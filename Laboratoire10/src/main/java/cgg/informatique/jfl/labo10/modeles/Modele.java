package cgg.informatique.jfl.labo10.modeles;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


/**
 * Abstract class<br><br>
 * fields:
 * <ul>
 * <li>id</li>
 * </ul>
 * @author alexandrearsenault
 *
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Modele {

	/**
	 *  An unique id for the DataBase
	 */
    @Id
    @GeneratedValue
    @Column(name = "Id", length=11)
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
