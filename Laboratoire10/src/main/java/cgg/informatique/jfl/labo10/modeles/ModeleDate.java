/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cgg.informatique.jfl.labo10.modeles;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


/**
 * Abstract class<br><br>
 * fields:
 * <ul>
 * <li>id</li>
 * <li>date</li>
 * </ul>
 * @author alexandrearsenault
 *
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ModeleDate extends Modele {
	
    /**
     * The modification date
     * (user expire if never confirm )
     */
    @Column(name = "Date") 
    protected Date date; 
    

	/**
	 * Get the last modification date  date.
	 * @return date The last modification date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Set the last modification date to the current date.
	 */
	public void setDate() {
		this.date = new Date();
	}
	
	/**
	 * Set the last modification date to the desired date.
	 * @param pDate The desired date.
	 * @return ok
	 */
	public boolean setDate(Date pDate) {
		boolean ok = validateDate(pDate);
		this.date = (ok? pDate :this.date);
		return ok;
	}
	
	
	/**
	 * Validate an alias.<br>
	 * <p>The policy for an object's date is:</p>
	 * <ul>
	 *  <li><p>XXXXXXXX</p></li>
	 *  <li><p>YYYYYYYY</p></li>
	 *	</ul>
	 * @param pDate The alias to validate
	 * @return ok
	 */
	public static boolean validateDate(Date pDate) {
		boolean ok = true;
		if (!ok) {
			//TODO real date validation according to policy
		}
        return ok;
	}
}
