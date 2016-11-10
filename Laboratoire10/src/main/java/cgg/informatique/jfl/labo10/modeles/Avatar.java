package cgg.informatique.jfl.labo10.modeles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "avatar.list", query = "select a from Avatar a")
              })
@XmlRootElement(name = "avatar")
public class Avatar extends Modele {
	
	@Column(name = "NOM", length=50 , nullable = false)
	/**
	 * A description for the avatar 
	 */
    private String name;
    
	/**
	 *  The avatar image in Base64
	 */
    @Column(name = "AVATAR")
    private String avatar;
	
    
    
    /**
     * DO NOT USE it is useless 
     * "Unenhanced classes must have a public or protected no-args constructor"
     */
    public Avatar(){
    }
    
    
    /**
     * Use this constructor instead. 2 params
     * 
     * @param pNom, A description for the avatar 
     * @param pAvatar,  The avatar image in Base64
     */
    public Avatar(String pNom , String pAvatar ){
		if(pNom.length()<= 50){
			this.name    = pNom;
			this.avatar = pAvatar;
		}
	}


    /**
     * Get the avatar's name ( description ) 
     * @return nom
     */
	public String getName() {
		return name;
	}

	/**
	 * Set the name (description) of the avatar. 
	 * @param pNom , The name to be set.
	 */
	
	public void setName(String pNom) {
		this.name = pNom;
	}

	/**
	 *  Get the avatar (image in Base64 )
	 * @return avatar,  a description for the avatar.
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 *  Set the avatar (image in Base64 )
	 * @param pAvatar, The avatar to be set
	 */
	public void setAvatar(String pAvatar) {
		this.avatar = pAvatar;
	}
	

	@Override
	public String toString() {
		return "Avatar named ':"+this.name+"' ";
	}

}
