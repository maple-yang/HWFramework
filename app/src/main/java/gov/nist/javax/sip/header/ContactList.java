package gov.nist.javax.sip.header;

import javax.sip.header.ContactHeader;

public class ContactList extends SIPHeaderList<Contact> {
    private static final long serialVersionUID = 1224806837758986814L;

    public Object clone() {
        ContactList retval = new ContactList();
        retval.clonehlist(this.hlist);
        return retval;
    }

    public ContactList() {
        super(Contact.class, ContactHeader.NAME);
    }
}
