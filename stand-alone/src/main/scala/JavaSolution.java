import java.util.ArrayList;
import java.util.List;

public class JavaSolution {
    public static void main(String[] args) {
        int r = solution(new int[]{5, 4, 0, 3, 1, 6, 2});
        System.out.println(r);
    }


    static public   int solution(int[] A) {
        // write your code in Java SE 8
        int max = 0;
        if (A.length == 0)
            return max;

        List<Integer> alreadyBead = new  ArrayList<Integer>();
        for(int i = 0; i < A.length; i++) {
            if (!alreadyBead.contains(i)){
              alreadyBead.add(i);
              int next = A[i];
              int currMax = 1;
              while (!alreadyBead.contains(next)){
                  alreadyBead.add(next);
                  currMax ++;
                  next = A[next];
              }
              if (currMax > max)
                  max = currMax;
            }
        }

        return max;

    }


}
