import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Random;

public class Viewer implements Runnable{


    // Enum to describe what state the viewers are in. WAITING describes when
    // they are at the party and can choose to view the vase. VIEWING is when
    // they are viewing the vase, and HOME describes when they are leaving and
    // can no longer view the vase.
    enum State{
        WAITING, VIEWING, HOME
    };

    ConcurrentLinkedQueue<Integer> queue;
    int guestNum;
    State state;
    Viewer[] guests;

    public Viewer(int guestNum, ConcurrentLinkedQueue<Integer> queue, Viewer[] guests){
        this.guestNum = guestNum;
        this.queue = queue;
        this.guests = guests;
        this.state = State.WAITING;
    }


    // Allow the exiting guest to tell the next one to enter.
    public void setState(State state){
        this.state = state;
    }

    @Override
    public void run(){

        // Randomly choose the chance they will enter the queue.
        Random rand = new Random(System.currentTimeMillis());
        double qChance = rand.nextDouble()/3;
        
        while(state != State.HOME){

            // While waiting, they choose to enter the queue every so often.
            if(state == State.WAITING){

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // If they choose to enter the queue, the chances of doing so
                // again decrease.
                if(rand.nextDouble() < qChance){
                    queue.add(guestNum);
                    qChance /= 3;
                }

            }
            
            // Randomly choose some time to view, then when they leave, they
            // tell the next guest to go view the vase.
            else if(state == State.VIEWING){
                System.out.println("Guest "+guestNum+" is viewing");
                int viewTime = (int)rand.nextDouble()*500;
                try {
                    Thread.sleep(viewTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (state != State.HOME){
                    state = State.WAITING;
                    if(!queue.isEmpty()){
                        int nextGuest = queue.poll();
                        guests[nextGuest].setState(State.VIEWING);
                    }
                }
            }
        }
    }
    
}
