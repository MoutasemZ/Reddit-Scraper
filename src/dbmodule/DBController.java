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
package dbmodule;

import DateTimeHelperLib.UtcDateConverter;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import reddit_scraper.PostInfo;

/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */
public class DBController {
    
    private Cluster CD_cluster = null;
    private Session CD_session = null;
    private PreparedStatement PI_In_Stat_prepared = null; // insert statment for inserting PostInfo
    private BoundStatement PI_In_Stat_bound = null;
    
    public DBController () {
        
       
            CD_cluster = Cluster.builder() // (1)
                    .addContactPoint("127.0.0.1")
                    .build();
            CD_session = CD_cluster.connect();
            initializeCQLStatements ();
            
        
    } // constructor
    
    private void initializeCQLStatements() {
        String PI_In_Stat = "insert into reddit_db.Submissions "
                + "(Author, Title, Post_Date, Post_Id, Body, subreddit, subreddit_Id, score, total_comments) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
          PI_In_Stat_prepared = CD_session.prepare(PI_In_Stat);
          PI_In_Stat_bound = PI_In_Stat_prepared.bind();
          
    
    }
    
    public void InsertPostInfo (PostInfo info) {
            
            
             PI_In_Stat_bound.setString(0, info.getAuthor());
             PI_In_Stat_bound.setString(1, info.getTitle());
             PI_In_Stat_bound.setTimestamp(2, UtcDateConverter.ConvertTimeStampToDate(info.getPost_Date()));
             PI_In_Stat_bound.setString(3, info.getPost_Id());
             PI_In_Stat_bound.setString(4, info.getBody());
             PI_In_Stat_bound.setString(5, info.getSubreddit());
             PI_In_Stat_bound.setString(6, info.getSubreddit_Id());
             PI_In_Stat_bound.setInt(7, info.getScore());
             PI_In_Stat_bound.setInt(8, info.getTotal_Comments()); 
           
             CD_session.execute(PI_In_Stat_bound);
         
    } // InsertPostInfo
    
    public void endSession () {
        
        if (CD_cluster != null) {
            CD_session.close();
            CD_cluster.close();
        }
    }
    
}
