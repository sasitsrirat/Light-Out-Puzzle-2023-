/* 6413110 Mr.Watcharsak Prommanee
6413112 Mr.Sasit Srirat
6413210 Mr.Kawin Kengkate
6413223 Mr.Ravipol Chayeraksa*/


import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class Game {

    private int N;
    private int allnum;
    private Light initial;
    private Light finish;
    private Graph<Light, DefaultEdge> G;

    public Game(String in, int n) {
        N = n;
        allnum = N * N;
        initial = new Light(in);
        String temp = "";
        for (int i = 0; i < allnum; i++) {
            temp = temp + "0";
        }
        finish = new Light(temp);
        G = new SimpleDirectedGraph<>(DefaultEdge.class);
        printBoard(initial);
    }

    public void Start() {
        ShortestPathAlgorithm<Light, DefaultEdge> shortestPath = null;
        shortestPath = new DijkstraShortestPath<>(G);
        List<Light> sol = new ArrayList<Light>();
        ArrayList<Light> AL = new ArrayList<Light>();
        Light temp;
        int i = 0;
        AL.add(initial);
        while (i < AL.size()) {
            temp = AL.get(i);
            if (!G.containsVertex(temp)) {
                G.addVertex(temp);
            }
            for (int j = 0; j < allnum; j++) {
                String newstate = Totoggle(temp, j);
                boolean have = false;
                for (Light vertex : G.vertexSet()) {
                    if (vertex.getpresent().equals(newstate)) {
                        vertex.setprevious(temp.getpresent(), j);
                        G.addEdge(temp, vertex);
                        have = true;
                    }
                }
                if (!have) {
                    Light newlight = new Light(newstate);
                    G.addVertex(newlight);
                    AL.add(newlight);
                    G.addEdge(temp, newlight);
                }
            }
            i++;
            
        }
        int round = 1;
    /*    for (Light vertex : G.vertexSet()) {
            
                  System.out.printf("   %s   ",vertex.getpresentconverse());
                  if(round == 7){
                       System.out.println("");
                       round = 0;
                  }
                  
             round++;     
        }*/
        if (!G.containsVertex(finish)) {
            System.out.println("Cannot solve");
        } else {
            sol = shortestPath.getPath(initial, finish).getVertexList();
            showsol(sol);
        }
    }

    public void addbroken(int row, int col) {
        int po = row * N + col;
        initial.setbroken(po);
        finish.setbroken(po);
        printBoard(initial);
    }

    public void showsol(List sol) {
        int move = sol.size() - 1;
        ArrayList<Light> solution = new ArrayList<Light>(sol);
        System.out.printf("\n%d moves to turn off all lights\n", move);
        for (int i = 0; i < move; i++) {
            String tog = solution.get(i).gettoggle(solution.get(i + 1));
            int po = solution.get(i).getpo(solution.get(i + 1));
            int row = po / N;
            int col = po % N;
            System.out.printf("\n>>> Move %d : turn %-3s row %d, col %d\n", i + 1, tog, row, col);
            printBoard(solution.get(i + 1));
        }
    }

    public void printBoard(Light L) {
        String temp = L.getpresent();
        System.out.printf("States in bits = ");
        for (int i = 0; i < allnum; i++) {
            switch (temp.charAt(i)) {
                case '0':
                    System.out.printf("0");
                    break;
                case '1':
                    System.out.printf("1");
                    break;
                case '2':
                    System.out.printf("0");
                    break;
                case '3':
                    System.out.printf("1");
                    break;
            }
        }
        System.out.printf("\n       ");
        for (int i = 0; i < N; i++) {
            System.out.printf("| col%2d ", i);
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf(" row%2d ", i);
            for (int j = 0; j < N; j++) {
                switch (temp.charAt((N * i) + j)) {
                    case '0':
                        System.out.printf("|   %-2s  ", "0");
                        break;
                    case '1':
                        System.out.printf("|   %-2s  ", "1");
                        break;
                    case '2':
                        System.out.printf("|   %-2s  ", "0x");
                        break;
                    case '3':
                        System.out.printf("|   %-2s  ", "1x");
                        break;
                }
            }
            System.out.println();
        }
    }

    public String Totoggle(Light L, int i) {
        Light temp = L;
        char board[] = temp.getpresent().toCharArray();

        board[i] = toggle(board[i]);
        if (board[i] == '2' || board[i] == '3') {
            if (i / N > 0 && i % N != 0) {
                board[i - N - 1] = toggle(board[i - N - 1]);
            }
            if (i / N > 0 && i % N != N - 1) {
                board[i - N + 1] = toggle(board[i - N + 1]);
            }
            if (i / N < N - 1 && i % N != 0) {
                board[i + N - 1] = toggle(board[i + N - 1]);
            }
            if (i / N < N - 1 && i % N != N - 1) {
                board[i + N + 1] = toggle(board[i + N + 1]);
            }
        } else {
            if (i % N != N - 1) {
                board[i + 1] = toggle(board[i + 1]);
            }
            if (i % N != 0) {
                board[i - 1] = toggle(board[i - 1]);
            }
            if (i / N < N - 1) {
                board[i + N] = toggle(board[i + N]);
            }
            if (i / N > 0) {
                board[i - N] = toggle(board[i - N]);
            }
        }
        String newstate = String.valueOf(board);
        return newstate;
    }

    public char toggle(char a) {
        switch (a) {
            case '0':
                a = '1';
                break;
            case '1':
                a = '0';
                break;
            case '2':
                a = '3';
                break;
            case '3':
                a = '2';
                break;
            default:
                System.out.printf("error\n");
                System.exit(0);
                break;
        }
        return a;
    }
}