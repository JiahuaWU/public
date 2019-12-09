package ch.epfl.sweng.tests;

// !!!!!!!!!!!!!!!!!!!!!!!!!
// You CAN edit this file.
// You CAN delete this file.
// !!!!!!!!!!!!!!!!!!!!!!!!!

import ch.epfl.sweng.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class StandardUserTests {
    private Forum forum;

    @Before
    public void setup() {
        forum = new Forum();
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateStandardUserWithNullName() {
        new StandardUser(null);
    }

    @Test
    public void standardUserCanAskLongQuestion() {
        User standard = new StandardUser("Standard");

        assertThat(standard.canAsk("0123456789"), is(true));
    }

    @Test
    public void standardUserCannontAskShortQuestion() {
        User standard = new StandardUser("Standard");
        assertThat(standard.canAsk("012345678"), is(false));
    }

    @Test
    public void standardUserCanAnswerQuestionFromOthers() {
        User alice = new StandardUser("Alice");
        User bob = new StandardUser("Bob");

        forum.postQuestion(alice, "Question text");
        Question question = forum.getQuestions().get(0);

        assertThat(bob.canAnswer(question, "Answer text"), is(true));
        assertThat(alice.canAnswer(question, "Answer text"), is(false));
    }

    @Test
    public void standardUserCanEditTheirPosts() {
        User standard = new StandardUser("Standard");
        User standard2 = new StandardUser("Standard2");
        forum.postQuestion(standard, "Question text");
        Question question = forum.getQuestions().get(0);

        // user can only edit their own post
        assertThat(standard.canEdit(question, "New question text"), is(true));
        assertThat(standard2.canEdit(question, "New question2 text"), is(false));

        // Cannot edit question once it has answer
        forum.postAnswer(standard2, question, "haha");
        assertThat(standard.canEdit(question, "New question text"), is(false));

        // users can edit their answers if someone posts >=2 answers for the same question
        Answer answer = question.getAnswers().get(0);
        assertThat(standard2.canEdit(answer, "New answer text"), is(false));
        forum.postAnswer(standard2, question, "hihi");
        forum.postAnswer(standard2, question, "heyhey");
        assertThat(standard2.canEdit(answer, "New answer text"), is(true));
    }

}