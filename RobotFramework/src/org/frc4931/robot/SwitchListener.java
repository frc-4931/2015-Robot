/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.frc4931.robot.component.Switch;

/**
 * 
 */
public enum SwitchListener implements Runnable{
    INSTANCE;
    private volatile AtomicBoolean running;
    private ConcurrentMap<Switch, Container> listeners;

    private SwitchListener() {
        listeners = new ConcurrentHashMap<Switch, Container>();
        running = new AtomicBoolean(false);
    }
    
    public void start(){
        running.set(true);
        new Thread(this).start();
    }
    
    public void stop(){
        running.set(false);
    }
    
    @Override
    public void run() {
        while(running.get())
            monitor();
    }
    
    public void monitor(){
        listeners.forEach((swtch, container)->container.update(swtch));
    }
    
    public void onTriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhenTriggered(function);
    }
    
    public void whileTriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhileTriggered(function);
    }
    
    public void onUntriggered(Switch swtch, ListenerFunction function){
        listeners.putIfAbsent(swtch, new Container()).addWhenUntriggered(function);
    }
    
    private static final class Container{
        private boolean previousState;
        private Node whenTriggered;
        private Node whileTriggered;
        private Node whenUntriggered;
        
        public Container(){}
        
        public void update(Switch swtch) {
            // Went from not triggered to triggered
            if(swtch.isTriggered()&&!previousState)
                whenTriggered.fire();
            
            // Went from triggered to not triggered
            else if(!swtch.isTriggered()&&previousState)
                whenUntriggered.fire();
            
            // Switch is still triggered
            else if(swtch.isTriggered()==previousState==true)
                whileTriggered.fire();
        }
        
        public void addWhenTriggered(ListenerFunction function) {
            whenTriggered = new Node(function, whenTriggered);
        }
        
        public void addWhileTriggered(ListenerFunction function) {
            whileTriggered = new Node(function, whileTriggered);
        }
        
        public void addWhenUntriggered(ListenerFunction function) {
            whenUntriggered = new Node(function, whenUntriggered);
        }
        
        private static final class Node{
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
    }

    @FunctionalInterface
    public static interface ListenerFunction {
        public void execute();
    }

}
