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

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */
public class PostInfo {
    
  @SerializedName("author")
  private String Author ;
  
  @SerializedName("title")
  private String Title ;
  
  @SerializedName("created_utc")
  private long Post_Date ;
  
  @SerializedName("id")
  private String Post_Id;
  
  @SerializedName("selftext")
  private String Body ;
  
  @SerializedName("subreddit")
  private String Subreddit;
  
  @SerializedName("subreddit_id")
  private String Subreddit_Id ;
  
  @SerializedName("score")
  private int Score ;
  
  @SerializedName("num_comments")
  private int Total_Comments ;  

   public  PostInfo () {
       
   }
   public PostInfo(String Author, String Title, long Post_Date, String Post_Id, String Body, String Subreddit, String Subreddit_Id, int Score, int Total_Comments) {
        this(); // call the default constrctor 
        this.Author = Author;
        this.Title = Title;
        this.Post_Date = Post_Date;
        this.Post_Id = Post_Id;
        this.Body = Body;
        this.Subreddit = Subreddit;
        this.Subreddit_Id = Subreddit_Id;
        this.Score = Score;
        this.Total_Comments = Total_Comments;
    }
   public String toCSVString () {
       return Author+','+Title + ','+String.valueOf(Post_Date)+','+Post_Id+','+Body+','+Subreddit+','+Subreddit_Id+','+String.valueOf(Score)+','+String.valueOf(Total_Comments);
       
   }
    /**
     * @return the Author
     */
    public String getAuthor() {
        return Author;
    }

    /**
     * @param Author the Author to set
     */
    public void setAuthor(String Author) {
        this.Author = Author;
    }

    /**
     * @return the Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * @param Title the Title to set
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * @return the Post_Date
     */
    public long getPost_Date() {
        return Post_Date;
    }

    /**
     * @param Post_Date the Post_Date to set
     */
    public void setPost_Date(long Post_Date) {
        this.Post_Date = Post_Date;
    }

    /**
     * @return the Post_Id
     */
    public String getPost_Id() {
        return Post_Id;
    }

    /**
     * @param Post_Id the Post_Id to set
     */
    public void setPost_Id(String Post_Id) {
        this.Post_Id = Post_Id;
    }

    /**
     * @return the Body
     */
    public String getBody() {
        return Body;
    }

    /**
     * @param Body the Body to set
     */
    public void setBody(String Body) {
        this.Body = Body;
    }

    /**
     * @return the Subreddit
     */
    public String getSubreddit() {
        return Subreddit;
    }

    /**
     * @param Subreddit the Subreddit to set
     */
    public void setSubreddit(String Subreddit) {
        this.Subreddit = Subreddit;
    }

    /**
     * @return the Subreddit_Id
     */
    public String getSubreddit_Id() {
        return Subreddit_Id;
    }

    /**
     * @param Subreddit_Id the Subreddit_Id to set
     */
    public void setSubreddit_Id(String Subreddit_Id) {
        this.Subreddit_Id = Subreddit_Id;
    }

    /**
     * @return the Score
     */
    public int getScore() {
        return Score;
    }

    /**
     * @param Score the Score to set
     */
    public void setScore(int Score) {
        this.Score = Score;
    }

    /**
     * @return the Total_Comments
     */
    public int getTotal_Comments() {
        return Total_Comments;
    }

    /**
     * @param Total_Comments the Total_Comments to set
     */
    public void setTotal_Comments(int Total_Comments) {
        this.Total_Comments = Total_Comments;
    }
    
    
    
}
