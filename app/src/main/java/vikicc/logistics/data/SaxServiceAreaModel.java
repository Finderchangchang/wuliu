package vikicc.logistics.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import vikicc.logistics.model.CodeModel;
/**
 * Created by Administrator on 2015/8/4.
 */
public class SaxServiceAreaModel{
    /**
     * SAX方式获取XML文件的内容
     *
     * @param inStream
     * @return
     * @throws Throwable
     */
    public List<CodeModel> getCodeList(InputStream inStream) throws Throwable {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        CodeParser areaParser = new CodeParser();
        parser.parse(inStream, areaParser);
        inStream.close();
        return areaParser.getAreaList();
    }

    /**
     * @author wmh
     */
    private final class CodeParser extends DefaultHandler {
        private List<CodeModel> list = null;
        private CodeModel codeModel = null;
        private String tagname;
        private String temp="";

        //开始解析XML文档
        @Override
        public void startDocument() throws SAXException {
            list = new ArrayList<CodeModel>();
            super.startDocument();
        }

        //开始处理节点,在这里获得节点属性值的（节点属性）
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            //System.out.println("........localname:" + localName);
            if (localName.equals("CodeModel")) {
                codeModel = new CodeModel();
                codeModel.setKey(attributes.getValue("Key").toString());
                //System.out.println("........Key:" + attributes.getValue("Key"));
                codeModel.setValue(attributes.getValue("Val"));
                //System.out.println("........Value:" + attributes.getValue("Val"));
            }
            this.tagname = localName;
            super.startElement(uri, localName, qName, attributes);
        }

        //处理没有属性的节点（节点内容）
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println("endxml;;;");
            if (localName.equals("CodeModel")) {
                //System.out.println("endxml;;list.add");
                list.add(codeModel);
                //System.out.println("areammodel------"+list.size());
            }
            codeModel=null;
            tagname=null;
        }

        @Override
        public void endDocument() throws SAXException {
            //System.out.println("enddocument");

        }

        public List<CodeModel> getAreaList() {
            //System.out.println("////////////////////////" + list.size());
            return list;
        }
    }

}
