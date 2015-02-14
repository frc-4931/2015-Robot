/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.frc4931.robot.commandnew.Scheduler.Requireable;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
/**
 * 
 */
public class CommandGroupTest {
    private Scheduler scheduler;
    private Queue<String> list;
    private Command[] c;
    private Command[] d;
    
    @Before
    public void beforeEach() {
        scheduler = Scheduler.getInstance();
        list = new LinkedList<>();
        TestCommand.reset();
        c = new Command[10];
        for(int i = 0; i < c.length; i++)
            c[i] = new TestCommand(list);
        DelayCommand.reset();
        d = new Command[10];
        for(int i = 0; i < d.length; i++)
            d[i] = new DelayCommand(list);
    }
    
    @Test
    public void shouldCancelTimedOutCommand() {
        scheduler.add(c[0]);
        scheduler.add(c[1]);
        scheduler.add(d[0], 10);
        
        assertThat(list).isEmpty();
        
        scheduler.step(0);
        
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "C0 init", "C0 exe", "C0 fin",
                "C1 init", "C1 exe", "C1 fin",
                "D0 init", "D0 exe"}));
        list.clear();
        
        scheduler.step(1);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "D0 exe"}));
        list.clear();
        
        scheduler.step(2);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "D0 exe"}));
        list.clear();
        
        scheduler.step(100);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "D0 fin"}));
        list.clear();
    }
    
    @Test
    public void shoudKillCommandThatUsesRequirementOfAnother() {
        Requireable required = new Requireable(){};
        CommandWithRequirement r0 = new CommandWithRequirement(required, 0, list, true);
        CommandWithRequirement r1 = new CommandWithRequirement(required, 1, list, false);
        CommandWithRequirement r2 = new CommandWithRequirement(required, 2, list, true);

        scheduler.add(r0);
        scheduler.add(r1);
        scheduler.add(r2);
        
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "R0 end", "R1 init", "R1 exe", "R1 end"}));
        list.clear();
    }
    
    @Test
    public void shoudKillCommandGroupThatUsesRequirementOfAnother() {
        Requireable required = new Requireable(){
            @Override public String toString() {
                return "R0";
            }
        };
        CommandWithRequirement r0 = new CommandWithRequirement(required, 0, list, true);
        CommandWithRequirement r1 = new CommandWithRequirement(required, 1, list, true);
        CommandWithRequirement r2 = new CommandWithRequirement(required, 2, list, true);
        RequiredGroup g = new RequiredGroup(r0, r1, r2);
        
        CommandWithRequirement r3 = new CommandWithRequirement(required, 3, list, false);
        scheduler.add(g);
        
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "R0 init", "R0 exe", "R0 end"}));
        list.clear();
        
        scheduler.add(r3);
        scheduler.step(1);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
                "R1 end", "R3 init", "R3 exe", "R3 end"}));
        list.clear();
        
        scheduler.step(2);
        assertThat(list).isEmpty();
    }
    
    private final class RequiredGroup extends CommandGroup {
        public RequiredGroup(CommandWithRequirement... commands) {
            sequentially(commands);
        }
    }
    
    @Test
    public void shouldTimeoutDiagramFromBoard(){
        scheduler.add(new DiagramFromBoard(), 3);
        // Nothing has executed yet
        assertThat(list).isEmpty();
        
        // First step should execute 0
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin"}));
        list.clear();
        
        // Second step is a branch, nothing was executed, fork was primed
        scheduler.step(1);
        assertThat(list).isEmpty();
        
        // Third step should execute 1 and 2 (parallelized) and 3 (just forked)
        scheduler.step(2);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C1 init", "C1 exe", "C1 fin",
            "C2 init", "C2 exe", "C2 fin",
            "C3 init", "C3 exe", "C3 fin"}));
        list.clear();
        
        // Fourth step should execute 5 (main) and 4 (on fork)
        scheduler.step(3);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C5 init", "C5 exe", "C5 fin",
            "C4 init", "C4 exe", "C4 fin"}));
        list.clear();
        
        // Fifth step is a branch, nothing was executed fork is dead, another fork was primed
        scheduler.step(4);
        assertThat(list).isEmpty();
        
        // Sixth step should execute 6 (main) and 7 (just forked)
        scheduler.step(5);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C6 init", "C6 exe", "C6 fin",
            "C7 init", "C7 exe", "C7 fin"}));
        list.clear();
        
        // Seventh step scheduler is empty, nothing executes
        scheduler.step(6);
        assertThat(list).isEmpty();
    }
    
    @Test
    public void shouldExecuteCommandsInOrder() {
        Command c = new SeqCommandGroup();
        scheduler.add(c);
        assertThat(list).isEmpty();
        // First step, C0 should have been added, run, and finished
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin"}));
        
        // Second step, C1 should have been added, run, and finished
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin",
            "C1 init", "C1 exe", "C1 fin"}));
        
        // Third step, C2 should have been added, run, and finished
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin",
            "C1 init", "C1 exe", "C1 fin",
            "C2 init", "C2 exe", "C2 fin"}));
    }
    
    private final class SeqCommandGroup extends CommandGroup {
        @SuppressWarnings("synthetic-access")
        public SeqCommandGroup (){
            sequentially(c[0],c[1],c[2]);
        }
    }
  
    @Test
    public void shouldExecuteCommandsTogether() {
        Command c = new SimulCommandGroup();
        scheduler.add(c);
        scheduler.step(0);
        // After one step, all three should have run
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin",
            "C1 init", "C1 exe", "C1 fin",
            "C2 init", "C2 exe", "C2 fin"}));
    }
    
    private final class SimulCommandGroup extends CommandGroup {
        @SuppressWarnings("synthetic-access")
        public SimulCommandGroup (){
            simultaneously(c[0],c[1],c[2]);
        }
    }
    
    @Test
    public void shouldExecuteTwoCommandsTogetherAndOneAfter() {
        Command c = new TwoOneGroup();
        scheduler.add(c);
        // After one step, first two should have run
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin",
            "C1 init", "C1 exe", "C1 fin"}));
        list.clear();
        
        // After two steps, all three should have run
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C2 init", "C2 exe", "C2 fin"}));
    }
    
    private final class TwoOneGroup extends CommandGroup {
        @SuppressWarnings("synthetic-access")
        public TwoOneGroup() {
            sequentially(simultaneously(c[0],c[1]),
            c[2]);
        }
    }
    
    @Test
    public void shouldModelDiagramFromBoard(){
        scheduler.add(new DiagramFromBoard());
        // Nothing has executed yet
        assertThat(list).isEmpty();
        
        // First step should execute 0
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin"}));
        list.clear();
        
        // Second step is a branch, nothing was executed, fork was primed
        scheduler.step(0);
        assertThat(list).isEmpty();
        
        // Third step should execute 1 and 2 (parallelized) and 3 (just forked)
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C1 init", "C1 exe", "C1 fin",
            "C2 init", "C2 exe", "C2 fin",
            "C3 init", "C3 exe", "C3 fin"}));
        list.clear();
        
        // Fourth step should execute 5 (main) and 4 (on fork)
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C5 init", "C5 exe", "C5 fin",
            "C4 init", "C4 exe", "C4 fin"}));
        list.clear();
        
        // Fifth step is a branch, nothing was executed fork is dead, another fork was primed
        scheduler.step(0);
        assertThat(list).isEmpty();
        
        // Sixth step should execute 6 (main) and 7 (just forked)
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C6 init", "C6 exe", "C6 fin",
            "C7 init", "C7 exe", "C7 fin"}));
        list.clear();
        
        // Seventh step scheduler is empty, nothing executes
        scheduler.step(0);
        assertThat(list).isEmpty();
    }
    
    private final class DiagramFromBoard extends CommandGroup {
        @SuppressWarnings("synthetic-access")
        public DiagramFromBoard() {
            sequentially(c[0],
                         fork(sequentially(
                                           c[3],
                                           c[4]
                                 )),
                                 simultaneously(c[1], c[2]),
                                 c[5],
                                 fork(c[7]),
                                 c[6]);
            }
    }
    @Test
    public void shouldModelOtherDiagramFromBoard() {
        scheduler.add(new OtherDiagramFromBoard());
        
        // Nothing has executed yet
        assertThat(list).isEmpty();
        
        // First step should execute 0
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C0 init", "C0 exe", "C0 fin"}));
        list.clear();
        
        // Second step is a branch, nothing executes, fork is primed
        scheduler.step(0);
        assertThat(list).isEmpty();
        
        // Third step should execute 3 (main) and 1 and 2 (just forked parallelized)
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C3 init", "C3 exe", "C3 fin",
            "C1 init", "C1 exe", "C1 fin",
            "C2 init", "C2 exe", "C2 fin"}));
        list.clear();
        
        // Fourth step should execute 4 and 5 (parallelized) fork is dead
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C4 init", "C4 exe", "C4 fin",
            "C5 init", "C5 exe", "C5 fin"}));
        list.clear();
        
        // Fifth step should execute 6
        scheduler.step(0);
        assertThat(list).isEqualTo(Arrays.asList(new String[]{
            "C6 init", "C6 exe", "C6 fin"}));
        list.clear();
        
        // Sixth step scheduler is empty, nothing executed
        scheduler.step(0);
        assertThat(list).isEmpty();
        
    }
    
    private final class OtherDiagramFromBoard extends CommandGroup {
        @SuppressWarnings("synthetic-access")
        public OtherDiagramFromBoard() {
            sequentially(c[0],
                         fork(simultaneously(c[1],c[2])),
                          c[3],
                          simultaneously(c[4],c[5]),
                          c[6]
                          );
            }
    }
    
    private static final class TestCommand extends Command {
        private static int commandID = 0;
        public static void reset() { commandID = 0; }
        private final Queue<String> list;
        private final int id;
        /**
         * @param list the list to log to
         */
        public TestCommand(Queue<String> list) {
            this.list = list;
            id = commandID;
            commandID++;
        }

        @Override
        public void initialize() {
            list.offer("C"+id+" init");
        }

        @Override
        public boolean execute() {
            list.offer("C"+id+" exe");
            return true;
        }

        @Override
        public void end() {
            list.offer("C"+id+" fin");
        }
        
        @Override
        public String toString() {
            return "C"+id;
        }
        
    }
    
    private static final class DelayCommand extends Command {
        private static int commandID = 0;
        public static void reset() { commandID = 0; }
        private final Queue<String> list;
        private final int id;
        /**
         * @param list the list to log to
         */
        public DelayCommand(Queue<String> list) {
            this.list = list;
            id = commandID;
            commandID++;
        }

        @Override
        public void initialize() {
            list.offer("D"+id+" init");
        }

        @Override
        public boolean execute() {
            list.offer("D"+id+" exe");
            return false;
        }

        @Override
        public void end() {
            list.offer("D"+id+" fin");
        }
        
        @Override
        public String toString() {
            return "D"+id;
        }
    }
    
    private final class CommandWithRequirement extends Command {
        private final Queue<String> list;
        private final int number;
        
        public CommandWithRequirement(Requireable required, int number, Queue<String> list, boolean interruptible) {
            this.list = list;
            this.number = number;
            if(!interruptible) setNotInterruptible();
            requires(required);
        }
    
        @Override
        public void initialize() {
            list.offer("R"+number+" init");
        }
    
        @Override
        public boolean execute() {
            list.offer("R"+number+" exe");
            return true;
        }
    
        @Override
        public void end() {
            list.offer("R"+number+" end");
        }
        
        @Override
        public String toString() {
            return "R"+number;
        }
    }
}
