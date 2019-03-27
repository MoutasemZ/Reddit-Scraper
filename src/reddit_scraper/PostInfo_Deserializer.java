/*
 * Copyright (C) 2019  Moutasem Zakkar, www.linkedin.com/in/mzakkar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package reddit_scraper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */
public class PostInfo_Deserializer  implements JsonDeserializer<PostInfo> {
    
  @Override
  public PostInfo deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
      
    JsonObject jsonObject = json.getAsJsonObject();

    PostInfo sInfo = new PostInfo();
    sInfo.setAuthor(jsonObject.get("author").getAsString());
    sInfo.setTitle(jsonObject.get("title").getAsString());
    sInfo.setPost_Date(jsonObject.get("created_utc").getAsLong());
    sInfo.setPost_Id (jsonObject.get("id").getAsString());
    
    if (jsonObject.has("selftext")) {
       sInfo.setBody (jsonObject.get("selftext").getAsString());
    }
    else {
        sInfo.setBody ("");
    }
    sInfo.setSubreddit (jsonObject.get("subreddit").getAsString());
    sInfo.setSubreddit_Id (jsonObject.get("subreddit_id").getAsString());
    sInfo.setScore (jsonObject.get("score").getAsInt());
    sInfo.setTotal_Comments (jsonObject.get("num_comments").getAsInt());
    
    return sInfo;
  }
    
    
    
    
}
