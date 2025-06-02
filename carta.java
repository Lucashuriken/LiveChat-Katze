import java.util.*;
import java.lang.*;
public class carta {
  
    public static void main(String[] args) {
        int tentativas = 0;
        double math;
        int kill = 0;
         while(kill != 1)
        {
            math = (Math.random()*10000);
            kill = (int) Math.round(math);
            tentativas ++;
        }
        System.out.println("Numero de mobs até dropar a paçoca: " + tentativas);
    }
}
