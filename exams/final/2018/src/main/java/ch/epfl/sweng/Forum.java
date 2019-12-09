package ch.epfl.sweng;

// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// You CANNOT edit or remove the signatures of the existing methods/constructors.
// You CANNOT add new constructors.
// You CAN change the implementation of the methods/constructors.
// You CAN add new methods.
// You CAN add interface implementations.
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Forum implements Observable{
    private final List<Question> questions;
    private List<Observer> observers;

    /**
     * Constructs an empty forum.
     */
    public Forum() {
        observers = new ArrayList<>();
        questions = new ArrayList<>();
    }

    /**
     * Gets the forum's questions.
     */
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    /**
     * Posts a question from the specified user with the specified text.
     *
     * @throws IllegalArgumentException  if the user or text is null.
     * @throws IllegalOperationException if the user cannot perform this operation.
     */
    public void postQuestion(User user, String text) {
        // TODO (remove the following if block, and write the rest of the implementation)
        if (user == null || text == null) {
            throw new IllegalArgumentException();
        }

        if (!user.canAsk(text)) {
            throw new IllegalOperationException();
        }
        notifyObservers(Arrays.asList(user, "postQuestion"));
        questions.add(new Question(user, text));
    }

    /**
     * Posts an answer from the specified user to the specified question with the specified text.
     *
     * @throws IllegalArgumentException  if the user, question or text is null.
     * @throws NoSuchPostException       if the question does not belong to the forum.
     * @throws IllegalOperationException if the user cannot perform this operation.
     */
    public void postAnswer(User user, Question question, String text) {
        // TODO (remove the following if block, and write the rest of the implementation)
        if (user == null || question == null || text == null) {
            throw new IllegalArgumentException();
        }

        if (!questions.contains(question)) {
            throw new NoSuchPostException();
        }

        if (!user.canAnswer(question, text)) {
            throw new IllegalOperationException();
        }
        notifyObservers(Arrays.asList(user, "postAnswer"));
        question.addAnswer(new Answer(question, user, text));
    }

    /**
     * Edits the specified post, as the specified user, with the specified new text.
     *
     * @throws IllegalArgumentException  if the user, post or text is null.
     * @throws NoSuchPostException       if the post does not belong to the forum.
     * @throws IllegalOperationException if the user cannot perform this operation.
     */
    public void editPost(User user, Post post, String text) {
        // TODO (remove the following if block, and write the rest of the implementation)
        if (user == null || post == null || text == null) {
            throw new IllegalArgumentException();
        }

        boolean NotbelongToForum = true;
        if (questions.contains(post)) {
            NotbelongToForum = false;
        } else {
            for (Question q: questions) {
                if (q.getAnswers().contains(post)) {
                    NotbelongToForum = false;
                    break;
                }
            }
        }
        if (NotbelongToForum) {
            throw new NoSuchPostException();
        }
        if (!user.canEdit(post, text)) {
            throw new IllegalOperationException();
        }

        post.setText(text);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(Object event) {
        observers.forEach(observer -> observer.update(this, event));
    }
}