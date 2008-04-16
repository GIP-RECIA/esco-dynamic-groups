package fr.esco.protocol.ldapsyn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.novell.ldap.LDAPIntermediateResponse;
import com.novell.ldap.asn1.ASN1OctetString;
import com.novell.ldap.asn1.ASN1Sequence;
import com.novell.ldap.asn1.ASN1Tagged;
import com.novell.ldap.rfc2251.RfcLDAPMessage;

/**
 * <h2>Implementation of the Sync Info Message.</h2><br/><br/>
 * <b><i>Extract from the RFC 4533 : </i></b><br/><br/>
 * 2.5.  Sync Info Message<br/>
 * <pre>
 * The Sync Info Message is an LDAP Intermediate Response Message
 * [RFC4511] where responseName is the object identifier
 * 1.3.6.1.4.1.4203.1.9.1.4 and responseValue contains a BER-encoded
 *  syncInfoValue.  The criticality is FALSE (and hence absent).
 *  
 *  syncInfoValue ::= CHOICE {
 *        newcookie      [0] syncCookie, 
 *         refreshDelete  [1] SEQUENCE {
 *             cookie         syncCookie OPTIONAL,
 *             refreshDone    BOOLEAN DEFAULT TRUE
 *         },
 *         refreshPresent [2] SEQUENCE {
 *             cookie         syncCookie OPTIONAL,
 *             refreshDone    BOOLEAN DEFAULT TRUE
 *         },
 *         syncIdSet      [3] SEQUENCE {
 *             cookie         syncCookie OPTIONAL,
 *             refreshDeletes BOOLEAN DEFAULT FALSE,
 *             syncUUIDs      SET OF syncUUID
 *         }
 *     }
 *     </pre>
 * @author GIP RECIA - A. Deman
 * 16 avr. 08
 */
public class SyncInfoMessage extends LDAPIntermediateResponse {

    /** OID of the message. */
    public static final String OID = "1.3.6.1.4.1.4203.1.9.1.4";

    /** Constant for the new cookie choice. */
    public static final int NEW_COOKIE = 0;

    /** Constant for the refresh delete choice. */
    public static final int REFRESH_DELETE = 1;

    /** Constant for the refresh present choice. */
    public static final int REFRESH_PRESENT = 2;

    /** Constant for the sync id choice. */
    public static final int SYNC_ID_SET = 3;
    
    /** Default value for the refreshDone Flag. */
    public static final boolean DEFAULT_REFRESH_DONE = true;

    /** Default value for the refreshDone Flag. */
    public static final boolean DEFAULT_REFRESH_DELETES = false;

    /** Value of the choice in the message. */
    private int choice;

    /** Cookie. */
    private byte[] cookie;

    /** Flag for refresh done. */
    private Boolean refreshDone;

    /** Flag for the refresh delete. */ 
    private Boolean refreshDeletes;

    /** List of uuids for the SYNC_ID_SET choice. */
    private List<byte[]> syncUUIDs;


    /**
     * Constructor for SyncInfoMessage
     * @param message The message to use.
     * @throws IOException
     */
    public SyncInfoMessage(final RfcLDAPMessage message) throws IOException {
        super(message);

        final ASN1Tagged tag = (ASN1Tagged) BERUtils.instance().getBERDecoder().decode(getValue());
        choice = tag.getIdentifier().getTag();
        final BERUtils berUtils = BERUtils.instance();


        if (choice == NEW_COOKIE) {
            // Choice 0: new cookie
            ASN1OctetString taggedValue = (ASN1OctetString) tag.taggedValue();
            cookie = taggedValue.byteValue();

        } else if (choice == REFRESH_DELETE || choice == REFRESH_PRESENT) {
            //Choices 1 or 2: refreshDElete or refreshPresent.

            final ASN1Sequence seq = berUtils.parseContentAsSequence(tag);
            cookie = berUtils.fetchCookie(seq, 0);

            int flagIndex = 1;
            if (cookie == null) {
                flagIndex --; 
            }
            refreshDone = berUtils.fetchFlag(seq, flagIndex, DEFAULT_REFRESH_DONE);  

        } else  if (choice == SYNC_ID_SET) {

            //  Choice 3 :  syncIdSet.
            final ASN1Sequence seq = berUtils.parseContentAsSequence(tag);
            cookie = berUtils.fetchCookie(seq, 0);

            int flagIndex = 1;
            if (cookie == null) {
                flagIndex --; 
            }
            refreshDeletes = berUtils.fetchFlag(seq, flagIndex, DEFAULT_REFRESH_DELETES);  
            
            // Retrieves the syncUUIDs.
            final int syncUUIDsIndex = seq.size() - 1;
            final ASN1Sequence syncUUIDsSeq = (ASN1Sequence) seq.get(syncUUIDsIndex);
            syncUUIDs = new ArrayList<byte[]>();
            for (int i = 0; i < syncUUIDsSeq.size(); i++) {
                final ASN1OctetString oStr = (ASN1OctetString) syncUUIDsSeq.get(i);
                syncUUIDs.add(oStr.byteValue());
            }
        }
    }

    /**
     * Gives the String representation of this message.
     * @return The String that represents this message.
     * @see com.novell.ldap.LDAPMessage#toString()
     */
    @Override
    public String toString() {
        String img = getClass().getSimpleName() + "# {";
        if (choice == REFRESH_PRESENT) {
            img += "REFRESH_PRESENT, " + cookie + ", " + refreshDone + "}" ;
        } else if (choice == REFRESH_DELETE) {
            img += "REFRESH_DELETE, " + cookie + ", " + refreshDone + "}";
        } else if (choice == NEW_COOKIE ) {
            img += "NEW COOKIE, " + cookie + "}";
        } else if (choice == SYNC_ID_SET) {
            img += "SYNC_ID_SET, " + cookie + ", " + refreshDeletes + "}";
        }
        return img;
    }
}