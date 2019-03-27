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
public class Post_list_Deserializer implements JsonDeserializer <Post_list> {
    
  @Override
  public Post_list deserialize( JsonElement json,  java.lang.reflect.Type typeOfT,  JsonDeserializationContext context)
      throws JsonParseException {
      
    JsonObject jsonObject = json.getAsJsonObject();
    
    PostInfo[] data = context.deserialize(jsonObject.get("data"), PostInfo[].class);
    Post_list myList = new Post_list() ;
    myList.setData(data); ;
    return myList;

  }
    
}
