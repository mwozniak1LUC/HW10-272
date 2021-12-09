import java.util.*;

public class DirectedNodeList{

    ArrayList<Integer> inList;
    ArrayList<Integer> outList;


    public DirectedNodeList() {
        inList = new ArrayList<>();
        outList = new ArrayList<>();
    }

    public void addToInList(int p) {
        inList.add(p);
    }

    public void addToOutList(int p) {
        outList.add(p);
    }

    public DirectedNodeList(ArrayList<Integer> in, ArrayList<Integer> out) {
        inList = in;
        outList=out;
    }

    public int getInDegree(){
        return inList.size();
    }

    public int getOutDegree(){
        return outList.size();
    }

    public ArrayList<Integer> getInList(){
        return inList;
    }

    public ArrayList<Integer> getOutList(){
        return outList;
    }


}
