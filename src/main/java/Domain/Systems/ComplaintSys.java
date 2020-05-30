package Domain.Systems;

import Data.SystemDB.DB;

import java.util.HashMap;

/**
 * The class represent the complaint system
 */
public class ComplaintSys {

    private HashMap<Integer, Complain> complains;
    private static int counter=0; //counter id to the complains

    /**
     * constructor
     */
    public ComplaintSys() {
        this.complains = new HashMap<>();
    }

    public Complain getComplain(int index){
        if(index>=this.complains.size()){
            throw new IllegalArgumentException("Complain does not exist");
        }
        return this.complains.get(index);
    }

    public String showComplain(int index){
        if(index>=this.complains.size()){
            throw new IllegalArgumentException("Complain does not exist");
        }
        return this.complains.get(index).toString();
    }

    /**
     * add new complain to the system
     * @param comp
     */
    public void addComplain(String comp, String username){
        complains.put(counter,new Complain(comp,username));
        counter++;
    }

    public void answerComplain(int index, String answer){
        if(index>=this.complains.size()){
            throw new IllegalArgumentException("Complain does not exist");
        }
        this.getComplain(index).setAnswer(answer);
        sendAnswerToUser(index, answer);
    }

    private void sendAnswerToUser(int index, String answer){
        DB.getInstance().getUser( this.getComplain(index).getUserName()).setComplainAnswer(this.getComplain(index));
    }

    public String showAllComplains(){
        StringBuilder allComplains=new StringBuilder();
        allComplains.append("Complains System:"+"\n\n");
        for (Integer index:complains.keySet()) {
            allComplains.append("Complain "+index+": "+complains.get(index).toString()+"\n\n");
        }
        return String.valueOf(allComplains);
    }

    public String showUnAnswerComplains(){
        StringBuilder unAnswerComplains=new StringBuilder();
        unAnswerComplains.append("Un Answer Complains:"+"\n\n");
        for (Integer index:complains.keySet()) {
            if(this.complains.get(index).getAnswer().equals("none")) {
                unAnswerComplains.append("Complain " + index + ": " + complains.get(index).toString() + "\n\n");
            }
        }
        return String.valueOf(unAnswerComplains);
    }



//        public static void main(String[] args) {
//        Complain comp1=new Complain("this is test","test1");
//        Complain comp2=new Complain("this is more test","test2");
//        ComplaintSys sys=new ComplaintSys();
//        sys.addComplain(comp1.getComplain(),comp1.getUserName());
//        sys.addComplain(comp2.getComplain(),comp2.getUserName());
//        sys.getComplain(0).setAnswer("answer!!!");
//        System.out.println(sys.showAllComplains());
//    }

}

