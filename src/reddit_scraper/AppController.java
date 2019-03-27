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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */

public class AppController {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Reddit_Reader myReddit = new Reddit_Reader("F:\\Open Data Sets\\Reddit Health\\");
        myReddit.ScrapeSubredditsList();
        
        
    }
    
}
