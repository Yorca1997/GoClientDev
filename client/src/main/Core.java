package src.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 刘俊延 on 2017/4/18.
 */
public class Core {

    private static boolean hasLiberty;
    private static boolean hasKilled;
    private static boolean hasKo;

    public static int action(int x, int y, int color) {
        Stone up = Board.stones[x][y].up();
        Stone down = Board.stones[x][y].down();
        Stone left = Board.stones[x][y].left();
        Stone right = Board.stones[x][y].right();
        hasLiberty = false;
        hasKilled = false;
        hasKo = false;
        if (up != null) {
            actionWith(x, y, up, color);
        }
        if (down != null) {
            actionWith(x, y, down, color);
        }
        if (left != null) {
            actionWith(x, y, left, color);
        }
        if (right != null) {
            actionWith(x, y, right, color);
        }
        if (hasKo) {
            return Action.INVALID;
        }
        return hasKilled ? Action.KILL : hasLiberty ? Action.PLACE : Action.INVALID;
    }

    public static ArrayList<Number> inventory(){
        double blackEyes = 0;
        double whiteEyes = 0;
        Set<Point> blackLiberty = new HashSet<>();
        Set<Point> whiteLiberty = new HashSet<>();
        for(int chain : Board.stoneMap.keySet()){
            if(Board.stoneMap.get(chain).size() != 0){
                for(Stone stone : Board.stoneMap.get(chain)){
                    if(stone.color == Stone.Black){
                        blackEyes += (double)Board.stoneMap.get(chain).size();
                        for(Point p : Board.liberty.get(chain)){
                            blackLiberty.add(p);
                        }
                    }
                    else if(stone.color == Stone.White){
                        whiteEyes += (double)Board.stoneMap.get(chain).size();
                        for(Point p : Board.liberty.get(chain)){
                            whiteLiberty.add(p);
                        }
                    }
                    break;
                }
            }
        }
        double common = 0;
        for(Point p : blackLiberty){
            if(whiteLiberty.contains(p)){
                common += 0.5;
            }
        }
        ArrayList<Number> result = new ArrayList<>();
        result.add(blackEyes + (double)blackLiberty.size() - common);
        result.add(whiteEyes + (double)whiteLiberty.size() - common);
        return result;
    }

    private static void actionWith(int x, int y, Stone stone, int color) {
        if (stone.color == Stone.None || (stone.color == color && liberty(stone) > 1)) {
            hasLiberty = true;
        } else if (stone.color == -color && liberty(stone) == 1) {
            if (!isKo(x, y, stone)) {
                hasKilled = true;
                Board.killed.add(Board.chainMap.get(stone));
            }
        }
    }

    private static boolean isKo(int x, int y, Stone stone) {
        if (x == Board.maybeKo[1].x && y == Board.maybeKo[1].y
                && stone.x == Board.maybeKo[0].x && stone.y == Board.maybeKo[0].y
                && stone.color == Board.maybeKo[0].color
                && Board.stoneMap.get(Board.chainMap.get(stone)).size() == 1) {
            hasKo = true;
            return true;
        }
        return false;
    }

    public static int liberty(Stone stone) {
        int chain = Board.chainMap.get(stone);
        System.out.println("Point (" + stone.x + "," + stone.y + ") in chain " + chain + " has liberty " + Board.liberty.get(chain).size());
        return Board.liberty.get(Board.chainMap.get(stone)).size();
    }

}
