/**
 * Implementations of the ldap sync protocol.
 */
package org.esco.dynamicgroups.ldap.ldapsync.protocol;

import com.novell.ldap.asn1.ASN1Boolean;
import com.novell.ldap.asn1.ASN1Object;
import com.novell.ldap.asn1.ASN1OctetString;
import com.novell.ldap.asn1.ASN1Sequence;
import com.novell.ldap.asn1.ASN1Tagged;
import com.novell.ldap.asn1.LBERDecoder;
import com.novell.ldap.asn1.LBEREncoder;



import java.io.ByteArrayInputStream;
import java.io.IOException;
/**
 * Utilities methods for the BER elements.
 * @author GIP RECIA - A. Deman
 * 16 avr. 08
 */
public class BERUtils {

    /** Singleton. */
    private static final BERUtils INSTANCE = new BERUtils();
    
    /** The BER decoder. */
    private LBERDecoder berDecoder = new LBERDecoder();
    
    /** The BER encoder. */
    private LBEREncoder berEncoder = new LBEREncoder();
    
    /**
     * Private Constructor for BERUtils.
     */
    private BERUtils() {
        /* PRIVATE. */
    }
    
    /**
     * Gives the singleton.
     * @return The INSTANCE of BERUtils.
     */
    public static BERUtils instance() {
        return INSTANCE;
    }
    
    /**
     * Tests if an ASN1 Object denotes an Octet String.
     * @param asn1Elt The object to test.
     * @return True if the object is an Octet String.
     */
    public boolean isOctetString(final ASN1Object asn1Elt) {
        return asn1Elt.getIdentifier().getTag() == ASN1OctetString.TAG;
    }
    
    /**
     * Tests if an ASN1 Object denotes a Boolean.
     * @param asn1Elt
     * @return True if the object is an Boolean.
     */
    public boolean isBoolean(final ASN1Object asn1Elt) {
        return asn1Elt.getIdentifier().getTag() == ASN1Boolean.TAG;
    }
    
    /**
     * Parses the content of the Tagged element as a Sequence.
     * @param tag The Tag to parse.
     * @return The Sequence extracted for the Tagged element.
     * @throws IOException
     */
    public ASN1Sequence parseContentAsSequence(final ASN1Tagged tag) throws IOException {
        ASN1OctetString taggedValue = (ASN1OctetString) tag.taggedValue();
        byte[] taggedContent = taggedValue.byteValue();
        return new ASN1Sequence(getBERDecoder(), new ByteArrayInputStream(taggedContent), taggedContent.length);
    }
    
    /**
     * Gives the BER decoder.
     * @return The decoder.
     */
    public LBERDecoder getBERDecoder() {
        return berDecoder;
    }
    
    /**
     * Gives the BER Encoder.
     * @return The Encoder.
     */
    public LBEREncoder getBEREncoder() {
        return berEncoder;
    }
    
    /**
     * Fetches the boolean value from an ASN1Object that denotes a ASN1Boolean.
     * The parameter is supposed to be effectively a ASN1Boolean (no check).
     * @param asn1Elt The ASN1Object used to retrieve the boolean value.
     * @return The boolean value.
     */
    boolean fetchBoolean(final ASN1Object asn1Elt) {
        return ((ASN1Boolean) asn1Elt).booleanValue();
    }

    /**
     * Fetches the byte array from an ASN1Object that denotes a ASN1OctetString.
     * The parameter is supposed to be effectively a ASN1OctetString (no check).
     * @param asn1Elt The ASN1Object used to retrieve the byte array.
     * @return The byte array value.
     */
    public byte[] fetchByteArray(final ASN1Object asn1Elt) {
        return ((ASN1OctetString) asn1Elt).byteValue();
    }
    
    /**
     * Extracts a cookie, if present, from a sequence.
     * @param seq The given sequence.
     * @param index The index of the cookie in the sequence, if it is present.
     * @return The cookie if found, null otherwise.
     */
    public byte[] fetchCookie(final ASN1Sequence seq, final int index) {
        byte[] cookieElt = null;
        if (seq.size() > index) {
            final ASN1Object asn1Elt = seq.get(index);
            if (BERUtils.instance().isOctetString(asn1Elt)) {
                cookieElt = BERUtils.instance().fetchByteArray(asn1Elt);
            }
        }
        return cookieElt;
    }
    
    /**
     * Extracts a boolean flag, if present, from a sequence.
     * @param seq The given sequence.
     * @param index The index of the booelan in the sequence, if it is present.
     * @param defaultValue The default value for the flag.
     * @return The flag if found, the default value otherwise.
     */
    public boolean fetchFlag(final ASN1Sequence seq, final int index, final boolean defaultValue) {
        if (seq.size() > index) {
            final ASN1Object asn1Elt = seq.get(index);
            if (isBoolean(asn1Elt)) {
                return fetchBoolean(asn1Elt);
            }
        }
        return defaultValue;
    }
}
