package utils;

import com.thoughtworks.xstream.XStream;

public class XMLTransformer {
    private XStream xstream = null;
    public static final String XMLDTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    private XMLTransformer() {
        xstream = new XStream();
    }

    public static XMLTransformer getInstance() {
        return new XMLTransformer();
    }

    public String toXMLString(Object object) {
        return XMLDTD + xstream.toXML(object);
    }

    public Object toObject(String xml) {
        return (Object) xstream.fromXML(xml);
    }
}
