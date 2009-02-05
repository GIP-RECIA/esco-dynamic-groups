package org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol;


import com.novell.ldap.LDAPControl;
import com.novell.ldap.asn1.ASN1Boolean;
import com.novell.ldap.asn1.ASN1Enumerated;
import com.novell.ldap.asn1.ASN1OctetString;
import com.novell.ldap.asn1.ASN1Sequence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <h2>Implementation of the SyncRequestControl.</h2><br/><br/>
 * <b><i>Extract from the RFC 4533 : </i></b><br/><br/>
 * 2.2.  Sync Request Control
 * <pre>
 * The Sync Request Control is an LDAP Control [RFC4511] where the
 * controlType is the object identifier 1.3.6.1.4.1.4203.1.9.1.1 and the
 * controlValue, an OCTET STRING, contains a BER-encoded
 * syncRequestValue.  The criticality field is either TRUE or FALSE.
 *
 *    syncRequestValue ::= SEQUENCE {
 *        mode ENUMERATED {
 *            -- 0 unused
 *            refreshOnly       (1),
 *            -- 2 reserved
 *            refreshAndPersist (3)
 *        },
 *        cookie     syncCookie OPTIONAL,
 *        reloadHint BOOLEAN DEFAULT FALSE
 *    }
 *
 * The Sync Request Control is only applicable to the SearchRequest
 *  Message.
 *</pre> 
 * @author GIP RECIA - A. Deman
 * 16 avr. 08
 *
 */
public class SyncRequestControl extends LDAPControl {

    /** OID of the control. */
    public static final String OID = "1.3.6.1.4.1.4203.1.9.1.1";

    /** Constant for the synchronization mode refreshOnly. */
    public static final int REFRESH_ONLY = 1;

    /** Constant for the synchronization mode refresh and persist. */
    public static final int REFRESH_AND_PERSIST = 3;

    /** Mode of synchronization. */
    private int mode;

    /** Cookie. */
    private byte[] cookie;

    /** Flag. */
    private boolean reloadHint;

    /** The string representation of the control.*/
    private String stringRepresentation;

    /**
     * 
     * Constructor for SyncRequestControl.
     * @param newMode The synchronization mode.
     * @param newReloadHint The flag.
     * @throws IOException
     */
    public SyncRequestControl(final int newMode, 
            final boolean newReloadHint) throws IOException {

        super(OID, true, null);
        this.mode = newMode;
        this.cookie = CookieManager.instance().getCurrentCookie();
        this.reloadHint = newReloadHint;

        // Builds the BER encoded value.
        ASN1Sequence asn1 = new ASN1Sequence();
        asn1.add(new ASN1Enumerated(mode));
        if (cookie != null) {
            asn1.add(new ASN1OctetString(cookie));
        }
        if (reloadHint) {
            asn1.add(new ASN1Boolean(reloadHint));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BERUtils.instance().getBEREncoder().encode(asn1, baos);
        setValue(baos.toByteArray());
    }

    /**
     * Gives the string representation of the control.
     * @return The string that denotes the values of the control.
     * @see com.novell.ldap.LDAPControl#toString()
     */
    @Override
    public String toString() {
        if (stringRepresentation == null) {
            stringRepresentation = getClass().getSimpleName() + "#{";
            if (mode == REFRESH_ONLY) {
                stringRepresentation += "REFRESH_ONLY, ";
            } else {
                stringRepresentation += "REFRESH_AND_PERSIST, ";
            }
            stringRepresentation += cookie + "," + reloadHint + "}";
        }
        return stringRepresentation;
    }
}

