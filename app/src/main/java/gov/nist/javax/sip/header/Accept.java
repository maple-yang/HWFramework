package gov.nist.javax.sip.header;

import gov.nist.core.Separators;
import gov.nist.javax.sip.header.ims.ParameterNamesIms;
import javax.sip.InvalidArgumentException;
import javax.sip.header.AcceptHeader;

public final class Accept extends ParametersHeader implements AcceptHeader {
    private static final long serialVersionUID = -7866187924308658151L;
    protected MediaRange mediaRange;

    public Accept() {
        super(AcceptHeader.NAME);
    }

    public boolean allowsAllContentTypes() {
        boolean z = false;
        if (this.mediaRange == null) {
            return false;
        }
        if (this.mediaRange.type.compareTo(Separators.STAR) == 0) {
            z = true;
        }
        return z;
    }

    public boolean allowsAllContentSubTypes() {
        boolean z = false;
        if (this.mediaRange == null) {
            return false;
        }
        if (this.mediaRange.getSubtype().compareTo(Separators.STAR) == 0) {
            z = true;
        }
        return z;
    }

    protected String encodeBody() {
        return encodeBody(new StringBuffer()).toString();
    }

    protected StringBuffer encodeBody(StringBuffer buffer) {
        if (this.mediaRange != null) {
            this.mediaRange.encode(buffer);
        }
        if (!(this.parameters == null || this.parameters.isEmpty())) {
            buffer.append(';');
            this.parameters.encode(buffer);
        }
        return buffer;
    }

    public MediaRange getMediaRange() {
        return this.mediaRange;
    }

    public String getContentType() {
        if (this.mediaRange == null) {
            return null;
        }
        return this.mediaRange.getType();
    }

    public String getContentSubType() {
        if (this.mediaRange == null) {
            return null;
        }
        return this.mediaRange.getSubtype();
    }

    public float getQValue() {
        return getParameterAsFloat(ParameterNamesIms.Q);
    }

    public boolean hasQValue() {
        return super.hasParameter(ParameterNamesIms.Q);
    }

    public void removeQValue() {
        super.removeParameter(ParameterNamesIms.Q);
    }

    public void setContentSubType(String subtype) {
        if (this.mediaRange == null) {
            this.mediaRange = new MediaRange();
        }
        this.mediaRange.setSubtype(subtype);
    }

    public void setContentType(String type) {
        if (this.mediaRange == null) {
            this.mediaRange = new MediaRange();
        }
        this.mediaRange.setType(type);
    }

    public void setQValue(float qValue) throws InvalidArgumentException {
        if (qValue == -1.0f) {
            super.removeParameter(ParameterNamesIms.Q);
        }
        super.setParameter(ParameterNamesIms.Q, qValue);
    }

    public void setMediaRange(MediaRange m) {
        this.mediaRange = m;
    }

    public Object clone() {
        Accept retval = (Accept) super.clone();
        if (this.mediaRange != null) {
            retval.mediaRange = (MediaRange) this.mediaRange.clone();
        }
        return retval;
    }
}
