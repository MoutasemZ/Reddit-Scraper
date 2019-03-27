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

import java.util.Comparator;

/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */
public class PostInfoComparator implements Comparator<PostInfo> {

    @Override
    public int compare(PostInfo o1, PostInfo o2) {
       
        return Long.compare(o1.getPost_Date(), o2.getPost_Date()) ;
    }
    
}
