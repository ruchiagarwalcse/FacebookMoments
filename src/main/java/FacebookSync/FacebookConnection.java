package FacebookSync;

import com.restfb.*;
import com.restfb.types.User;
import com.restfb.types.FriendList;
import com.restfb.batch.BatchRequest;
import com.restfb.batch.BatchResponse;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.restfb.batch.BatchRequest.BatchRequestBuilder;
import facebook4j.internal.org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpMethod;
import java.io.IOException;
import java.util.Date;
import com.restfb.types.Post;
//import com.restfb.scope.ScopeBuilder;


@RestController
public class FacebookConnection {

        private static String appId = "403024159903643";
        private static String appSecretId = "15b0bf950c65802d807eb71ac932820a";
        private static String redirectUrl   = "http://localhost:8080/welcome.jsp/action";

        @RequestMapping(name = "welcome.jsp/action", method = RequestMethod.GET)
        public ArrayList<String>  facebookInit(@RequestParam(value="code") String code) throws IOException{
            FacebookClient.AccessToken token = getFacebookUserToken(code, redirectUrl);
            String accessToken = token.getAccessToken();
            Date expires = token.getExpires();
            FacebookClient fbClient = new DefaultFacebookClient(accessToken);
            User me = fbClient.fetchObject("me",com.restfb.types.User.class);
            ArrayList<String> arr = new ArrayList<String>();
            //arr.add(me.getFirstName());
            Connection<Post> rPosts = fbClient.fetchConnection("me/posts", Post.class);
            for (Post p: rPosts.getData()){
                arr.add(p.getMessage());
            }
            return arr;
            //JSONObject fList = fbClient.fetchObject("me/taggable_friends",JSONObject.class);
            //System.out.println(fList);
            //return fList;
            //return "Ruchi";
        }
        private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
            /*ScopeBuilder scopeBuilder = new ScopeBuilder();
            scopeBuilder.addPermission(UserDataPermissions.USER_STATUS);
            scopeBuilder.addPermission(UserDataPermissions.USER_ABOUT_ME); */
            WebRequestor wr = new DefaultWebRequestor();
            WebRequestor.Response accessTokenResponse = wr.executeGet("https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl + "&client_secret=" + appSecretId + "&code=" + code);
            return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
        }


   /* public static void main(String[] args) {
        //String appId = "403024159903643";
        //String appSecret = "15b0bf950c65802d807eb71ac932820a";

        FacebookClient fbClient = new DefaultFacebookClient("CAACEdEose0cBAIckK9r7355XDPgyetocsqKZCkyQURHMj8g9ZACtBpkPsLqoAt2VIc7V5S6jROqjhfsRl3yoDH9naXfiZBv4N6TSl8r4DaDQnURoM5gwGDJ10dR1ai30mC6LgUtRZB56gbi8aeO0H6m6BSGcr9ueZBSSi0nNpoXvCSXalMl4iVPMaeQNqZBwxkCKVnxhllvO3vRw6oycZACUeVeIcwwXeUZD");
        User me = fbClient.fetchObject("749936265121703",com.restfb.types.User.class);
        FriendList fList = fbClient.fetchObject("749936265121703",com.restfb.types.FriendList.class);
        System.out.println(me.getEducation());
        System.out.println();
        BatchRequest meRequest = new BatchRequestBuilder("me").build();
        BatchRequest friendsRequest = new BatchRequestBuilder("me/friends").build();
        List<BatchResponse> results = fbClient.executeBatch(meRequest, friendsRequest);
        BatchResponse resultUser = results.get(0);
        BatchResponse resultFriendList = results.get(1);
        JsonMapper jsonMapper = new DefaultJsonMapper();
        User me = jsonMapper.toJavaObject(resultUser.getBody(), User.class);
        FriendList fList =  jsonMapper.toJavaObject(resultFriendList.getBody(), FriendList.class);
        System.out.println(fList);

    }        */
}
