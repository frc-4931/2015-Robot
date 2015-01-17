/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.frc4931.robot.component.Switch;

/**
 * A class that allows {@link ListenerFunction}s to be registered with {@link Switch}es
 * to execute on a certain change of state.
 */
public class SwitchListener{
    private static final SwitchListener INSTANCE = new SwitchListener();
    private final ConcurrentMap<Switch, Container> listeners;
    private volatile boolean running;
    private Thread monitorThread;

    public static SwitchListener getInstance() {
        return INSTANCE;
    }
    
    private SwitchListener() {
        listeners = new ConcurrentHashMap<>();
        running = false;
    }
    
    private void monitor(){
        listeners.forEach((swtch, container)->container.update(swtch));
    }

    /**
     * Starts monitoring the registered listeners in a new thread.
     */
    public void start(){
        if(running) return;
        running = true;
        monitorThread = new Thread(()->{
                while(running) monitor();
            },"Switch Listener Thread");
        monitorThread.start();
    }
    
    /**
     * Stops monitoring the register listeners.
     */
    public void stop(){
        running = false;
    }
    
    /**
     * Register a {@link ListenerFunction} to be called the moment when the specified
     * {@link Switch} is triggered.
     * @param swtch the {@link Switch} to bind the command to
     * @param function the {@link ListenerFunction} to execute
     */
    public void onTriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhenTriggered(function);
    }
    
    /**
     * Register a {@link ListenerFunction} to be called the moment when the specified
     * {@link Switch} is untriggered.
     * @param swtch the {@link Switch} to bind the command to
     * @param function the {@link ListenerFunction} to execute
     */
    public void onUntriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhenUntriggered(function);
    }
    
    /**
     * Register a {@link ListenerFunction} to be called repeatedly while the specified
     * {@link Switch} is triggered.
     * @param swtch the {@link Switch} to bind the command to
     * @param function the {@link ListenerFunction} to execute
     */
    public void whileTriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhileTriggered(function);
    }

    /**
     * Register a {@link ListenerFunction} to be called repeatedly while the specified
     * {@link Switch} is not triggered.
     * @param swtch the {@link Switch} to bind the command to
     * @param function the {@link ListenerFunction} to execute
     */
    public void whileUntriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhileUntriggered(function);
    }
    
    private static final class Container{
        private boolean previousState;
        private Node whenTriggered;
        private Node whenUntriggered;
        private Node whileTriggered;
        private Node whileUntriggered;
        
        public Container(){}
        
        public void update(Switch swtch) {
            if(whenTriggered!=null)
                // Went from not triggered to triggered
                if(swtch.isTriggered()&&!previousState)
                    whenTriggered.fire();
            
            if(whenUntriggered!=null)
                // Went from triggered to not triggered
                if(!swtch.isTriggered()&&previousState)
                    whenUntriggered.fire();
            
            if(whileTriggered!=null)
                // Switch was and is triggered
                if(swtch.isTriggered()&&previousState)
                    whileTriggered.fire();
            
            if(whileUntriggered!=null)
                // Switch wasn't and still isn't triggered
                if(!swtch.isTriggered()&&!previousState)
                    whileUntriggered.fire();
            
            previousState = swtch.isTriggered();
        }
        
        public void addWhenTriggered(ListenerFunction function) {
            whenTriggered = new Node(function, whenTriggered);
        }
        
        public void addWhenUntriggered(ListenerFunction function) {
            whenUntriggered = new Node(function, whenUntriggered);
        }
        
        public void addWhileTriggered(ListenerFunction function) {
            whileTriggered = new Node(function, whileTriggered);
        }
        
        public void addWhileUntriggered(ListenerFunction function) {
            whileUntriggered = new Node(function, whileUntriggered);
        }
        
        
    }
    
    protected static final class Node{
        private ListenerFunction function;
        private Node next;
        
        public Node(ListenerFunction function, Node next){
            this.function = function;
            this.next = next;
        }
        
        public void fire(){
            function.execute();
            if(next!=null)
                next.fire();
        }
    }

    /**
     * Functional interface that can be registered with {@link SwitchListener} to execute a
     * statement on a certain event.
     */
    @FunctionalInterface
    public static interface ListenerFunction {
        public void execute();
    }

}
