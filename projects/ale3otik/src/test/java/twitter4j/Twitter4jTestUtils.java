package twitter4j;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Dirty hack to access twitter4j package local classes.
 * <p>
 * Created by kormushin on 29.09.15.
 */
public class Twitter4jTestUtils {

    public static List<Status> tweetsFromJson(String resource) {
        try (InputStream inputStream = Twitter4jTestUtils.class.getResourceAsStream(resource)) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            JSONObject json = new JSONObject(CharStreams.toString(streamReader));

            JSONArray array = json.getJSONArray("statuses");
            List<Status> tweets = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(new StatusJSONImpl(tweet));
            }

            return tweets;
        } catch (IOException | JSONException | TwitterException e) {
            throw new RuntimeException(e);
        }
    }
}