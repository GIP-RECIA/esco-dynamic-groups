package org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol;

import com.novell.ldap.LDAPControl;
import com.novell.ldap.asn1.ASN1Sequence;

/**
 * <h2>Implementation of the Sync Don Control Message.</h2><br/><br/>
 * <b><i>Extract from the RFC 4533 : </i></b><br/>
 * <pre>
 * 2.4.  Sync Done Control
 *
 * The Sync Done Control is an LDAP Control [RFC4511] where the
 * controlType is the object identifier 1.3.6.1.4.1.4203.1.9.1.3 and the
 * controlValue contains a BER-encoded syncDoneValue.  The criticality
 * is FALSE (and hence absent).
 *
 *    syncDoneValue ::= SEQUENCE {
 *        cookie          syncCookie OPTIONAL,
 *        refreshDeletes  BOOLEAN DEFAULT FALSE
 *    }
 *
 * The Sync Done Control is only applicable to the SearchResultDone
 * Message.
 * </pre>
 * @author GIP RECIA - A. Deman
 * 16 avr. 08
 */
public class SyncDoneControl extends LDAPControl {
    /** OI of the control. */
	public static final String OID = "1.3.6.1.4.1.4203.1.9.1.3";
	
	/** Default value for the refreshDeletes flag. */
	public static final boolean DEFAULT_REFRESH_DELETES = false;

	/** Cookie. */
	private byte[] cookie;
	
	/** Flag. */
	private boolean refreshDeletes;
	
	/** String representation of the control. */
	private String stringRepresentation;

	/**
	 * Constructor for SyncDoneControl. 
	 * @param oid OID of the control.
	 * @param critical False for this control.
	 * @param value The value of the control (BER Encoded).
	 */
	public SyncDoneControl(final String oid, final boolean critical, final byte[] value) {
		super(oid, critical, value);
		if (value == null) {
		    cookie = null;
		    refreshDeletes = false;
		} else {
		    final BERUtils berUt = BERUtils.instance();
		    final ASN1Sequence seq = (ASN1Sequence) berUt.getBERDecoder().decode(value);
		    cookie = berUt.fetchCookie(seq, 0);
		    int flagIndex = 1;
		    if (cookie == null) {
		        flagIndex--;
		    }
		    refreshDeletes = berUt.fetchFlag(seq, flagIndex, DEFAULT_REFRESH_DELETES);
		}
	}
	
	/**
	 * Gives the string representation of the control.
	 * @return The String representation of the control.
	 * @see com.novell.ldap.LDAPControl#toString()
	 */
	@Override
	public String toString() {
	    if (stringRepresentation == null) {
	        stringRepresentation = getClass().getSimpleName() + "#{" + cookie + ", " + refreshDeletes + "}";
	    }
	    return stringRepresentation;
	}

    /**
     * Getter for cookie.
     * @return cookie.
     */
    public final byte[] getCookie() {
        return cookie;
    }

    /**
     * Setter for cookie.
     * @param cookie the new value for cookie.
     */
    public final void setCookie(final byte[] cookie) {
        this.cookie = cookie;
    }
}