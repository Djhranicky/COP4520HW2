import java.util.concurrent.atomic.AtomicBoolean;

public class Guest implements Runnable{

    enum State{
        WAITING, INMAZE, WIN
    };


    enum Problem{
        PARTY, VASE
    };

    
    private State state;
    private Boolean replaced;
    private int count;
    Problem problem;
    int numGuests;
    Boolean leader;
    AtomicBoolean cupcake;

    public Guest(Boolean leader, AtomicBoolean cupcake, int numGuests, Problem problem){
        this.leader = leader;
        this.cupcake = cupcake;
        this.numGuests = numGuests;
        this.state = State.WAITING;
        this.replaced = false;
        this.count = 0;
        this.problem = problem;
    }

    public State getState(){
        return this.state;
    }

    public void setState(State state){
        this.state = state;
    }

    @Override
    public void run(){
        if(problem == Problem.PARTY){
            while(state != State.WIN){
                if(leader){
                    if(state == State.WAITING){
                        continue;
                    }
                    else if(state == State.INMAZE){
                        if(count < numGuests && cupcake.compareAndSet(true, false)){
                            count++;
                        }
                        else if(count == numGuests){
                            BirthdayParty.declare();
                        }
                        if(state != State.WIN){
                            state = State.WAITING;
                        }
                    }
                }
                else{
                    if(state == State.WAITING){
                        continue;
                    }
                    else if(state == State.INMAZE){
                        if(replaced == false && cupcake.compareAndSet(false, true)){
                            replaced = true;
                        }
                        if(state != State.WIN){
                            state = State.WAITING;
                        }
                    }
                }
            }
        }
    }
}
