package ru.mipt.diht.students.ale3otik.moduletests.library;

import com.beust.jcommander.JCommander;
import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.mipt.diht.students.ale3otik.twitter.TimeDeterminer;
import ru.mipt.diht.students.ale3otik.twitter.TwitterClientArguments;
import ru.mipt.diht.students.ale3otik.twitter.TwitterUtils;
import twitter4j.Status;
import twitter4j.Twitter4jTestUtils;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by alex on 08.11.15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TimeDeterminer.class})
public class TwitterUtilTest {
    private static final int SEPARATOR_LENGTH = 80;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_BOLD = "\033[1m";

    private static final String expected = ANSI_BLUE + ANSI_BOLD
            + "@HeysannaHosanna" + ANSI_RESET + ANSI_RESET + ": ретвитнул "
            + ANSI_BLUE + ANSI_BOLD + "@PeterReinert" + ANSI_RESET + ANSI_RESET
            + ": The first biosensor chips based on graphene and its derivatives were presented"
            + " by the MIPT at the Open Innovations… http…(" + ANSI_BOLD + "2"
            + ANSI_RESET
            + " ретвита)\n"
            + Strings.repeat("-", 80);

    private List<Status> statuses;
    private TwitterClientArguments argsWithoutStream;
    private TwitterClientArguments argsWithStream;
    private Status retweetStatus;

    @Before
    public void setUp() {
        argsWithoutStream = new TwitterClientArguments();
        JCommander jcm1 = new JCommander(argsWithoutStream);
        jcm1.parse();

        argsWithStream = new TwitterClientArguments();
        JCommander jcm2 = new JCommander(argsWithStream);
        jcm2.parse("-s");

        statuses = Twitter4jTestUtils.tweetsFromJson("/MIPT.json");
        PowerMockito.mockStatic(TimeDeterminer.class);
        PowerMockito.when(TimeDeterminer.getTimeDifference(any(Date.class))).thenReturn("вчера");

        for (Status s : statuses) {
            if (s.isRetweet()) {
                retweetStatus = s;
                break;
            }
        }
    }

    @Test
    public void testGettingSplitLine() {
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < SEPARATOR_LENGTH; ++i) {
            separator.append("-");
        }
        assertThat(TwitterUtils.getSplitLine(), equalTo(separator.toString()));
    }

    @Test
    public void testGetFormattedTweetToPrintWithoutStream() {
        String answer = TwitterUtils.getFormattedTweetToPrint(retweetStatus, argsWithoutStream);
        assertThat("[вчера] " + expected, equalTo(answer));
    }

    @Test
    public void testGetFormattedTweetToPrintWithStream() {
        String answer = TwitterUtils.getFormattedTweetToPrint(retweetStatus, argsWithStream);
        assertThat(answer, equalTo(expected));
    }
}