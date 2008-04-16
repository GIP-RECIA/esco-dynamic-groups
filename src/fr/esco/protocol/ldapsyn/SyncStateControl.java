package fr.esco.protocol.ldapsyn;

import com.novell.ldap.LDAPControl;
import com.novell.ldap.asn1.ASN1Enumerated;
import com.novell.ldap.asn1.ASN1Sequence;

/**
 * <h2>Implementation of the Sync State Control.</h2><br/><br/>
 * <b><i>Extract from the RFC 4533 : </i></b><br/><br/>
 * 2.3.  Sync State Control
 * <pre>
 * The Sync State Control is an LDAP Control [RFC4511] where the
 * controlType is the object identifier 1.3.6.1.4.1.4203.1.9.1.2 and the
 * controlValue, an OCTET STRING, contains a BER-encoded syncStateValue.
 * The criticality is FALSE.
 *
 *    syncStateValue ::= SEQUENCE {
 *        state ENUMERATED {
 *            present (0),
 *            add (1),
 *            modify (2),
 *            delete (3)
 *        },
 *        entryUUID syncUUID,
 *        cookie    syncCookie OPTIONAL
 *    }
 *
 * The Sync State Control is only applicable to SearchResultEntry and
 * SearchResultReference Messages.
 * </pre>
 * 
 * 
 * @author GIP RECIA - A. Deman
 * 16 avr. 08
 *
 */
public class SyncStateControl extends LDAPControl {
    
    /** Constant for state, PRESENT value. */
    public static final int PRESENT = 0;
    
    /** Constant for state, ADD value. */
    public static final int ADD = 1;
    
    /** Constant for state, MODIFY value. */
    public static final int MODIFY = 2;
    
    /** Constant for state, DELETE value. */
    public static final int DELETE = 3;
    
    /** OID of the controller. */
    public static final String OID = "1.3.6.1.4.1.4203.1.9.1.2";

    /** Cookie. */
    private byte[] cookie;

    /** State. */
    private int state;
    
    /** UUID of the entry. */
    private byte[] entryUUID;
    
    /**
     * Constructor for SyncStateControl.
     * @param oid The OID of the Control.
     * @param critical False for this control.
     * @param value The BER encoded value.
     */
    public SyncStateControl(final String oid, final boolean critical, final byte[] value) {
        super(oid, critical, value);
        if (value == null) {
            cookie = null;
        } else {
            final int entryUUIDIndex = 1;
            final int cookieIndex = 2;
            final BERUtils berUt = BERUtils.instance();
            final ASN1Sequence seq = (ASN1Sequence) berUt.getBERDecoder().decode(value);
            final ASN1Enumerated stateEnum = (ASN1Enumerated) seq.get(0);
            state = stateEnum.intValue();
            
            entryUUID = berUt.fetchByteArray(seq.get(entryUUIDIndex));
            cookie =  berUt.fetchCookie(seq, cookieIndex);
            System.out.println("==> dans le control SyncStateControl value=" + this);
        }
        
    }

    /**
     * Gives the string representation of this control.
     * @return The string that represents the control.
     * @see com.novell.ldap.LDAPControl#toString()
     */
    @Override
    public String toString() {
        String img=getClass().getSimpleName() + "#{";
        if (state == ADD) {
            img += "ADD, ";
        } else if (state == MODIFY) {
            img += "MODIFY, ";
        } else if (state == PRESENT) {
            img += "PRESENT, ";
        } else if (state == DELETE) {
            img += "DELETE, ";
        } else {
            img += "UNDEFINED STATE, ";
        }
        img += entryUUID + ", ";
        img += cookie + " }";
        return img;
    }
}
