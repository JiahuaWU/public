package ch.epfl.sweng;

// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// You CANNOT edit or remove the signatures of the existing methods/constructors.
// You CANNOT add new constructors.
// You CAN change the implementation of the methods/constructors.
// You CAN add new methods.
// You CAN add interface implementations.
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

import java.util.*;

/**
 * A leaderboard that counts points.
 * The points are tallied as follows:
 * - Posting a question is worth 5 points.
 * - Posting an answer is worth 1 point.
 */
public final class Leaderboard implements Observer{
    private Forum forum;
    private HashMap<User, Integer> points;
    /**
     * Constructs a leaderboard for the specified forum.
     *
     * @throws IllegalArgumentException if the forum is null.
     */
    public Leaderboard(Forum forum) {
        this.forum = forum;
        forum.addObserver(this);
        points = new HashMap<>();
        if (forum == null) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Prints the leaderboard, using the following rules:
     * - Users are sorted by their point.
     * - If users have the same number of points, they are sorted by alphabetical order of names.
     * - Each user is printed as one line on the leaderboard.
     * - The line format is the following:
     * --- The line begins with a hash sign '#'
     * --- Immediately after the sign comes the user's rank.
     * ----- The user with most points has rank 1, the next user has rank 2, and so on.
     * ----- Two users with the same number of points have the same rank.
     * ------ If there are N users with the same number of points, N-1 ranks are skipped afterwards.
     * --- After the rank comes a space.
     * --- After that comes the user's name.
     * --- After the name comes a space.
     * --- After that comes the number of points.
     * - There is no line separator at the end, only between lines.
     * ===
     * [Begin example 0]
     * #1 George 9001
     * [End example 0]
     * [Begin example 1]
     * #1 George 9001
     * #2 Alice 100
     * #3 Bob 5
     * [End example 1]
     * [Begin example 2]
     * #1 George 9001
     * #2 Alice 100
     * #2 Carol 100
     * #4 Bob 5
     * [End example 2]
     */
    @Override
    public String toString() {
        String res = "";
        List<Map.Entry<User, Integer> > list = new LinkedList<>(points.entrySet());

        // Sort the list
        list.sort(Map.Entry.comparingByValue());

        int count = 1;
        for (int i = list.size() - 1; i > 0; i--) {
            int innerCount = 1;
            res += "#" + count + " " + list.get(i).getKey().getName() + " " + list.get(i).getValue() + "\n";
            while (i > 1 && list.get(i).getValue() == list.get(i - 1).getValue()) {
                i--;
                innerCount++;
                res += "#" + count + " " + list.get(i).getKey().getName() + " " + list.get(i).getValue() + "\n";
            }
            count += innerCount;
        }
        res += "#" + count + " " + list.get(0).getKey().getName() + " " + list.get(0).getValue();
        return res;
    }

    @Override
    public void update(Observable observable, Object arg) {
        List arg_list = (List) arg;
        User u = (User) arg_list.get(0);
        String op = (String) arg_list.get(1);
        if (!points.containsKey(u)) {
            points.put(u, 0);
        }
        if (op.equals("postQuestion")) {
            points.put(u, points.get(u) + 5);
        } else if (op.equals("postAnswer")) {
            points.put(u, points.get(u) + 1);
        }

    }
}
