package AutoHeal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.util.StringUtils;

/**
 * @author info2890
 *
 */

public class XpathCapture {

    private static int getElementPosition(final Element element) {
        final Elements chlds = element.parent().children();
        int count = 0;
        for (Element e : chlds) {
            if (e.nodeName().equals(element.nodeName())) {
                count++;
            }
        }
        int position = 0;
        for (Element e : chlds) {
            if (e.nodeName().equals(element.nodeName())) {
                position++;
                if (element.siblingIndex() == e.siblingIndex() && count > 1) {
                    break;
                }
            }
        }
        return count > 1 ? position : 0;
    }

    public void xpath(final String html, WebDriver driver, boolean b) {
    	try {
    	Document doc;
    	if(b) {
    		GeneratePageSourceToWord.storeSourceCodeToWord(html);
    		doc = Jsoup.parse(html);
    	}else {
    		final String html1 = PageSourceFromWord.fetchContent();
    		doc = Jsoup.parse(html1);
    	}
    	
        Elements elements = doc.body().getAllElements();
        int count = 0;
        ArrayList<String> xpathList = new ArrayList<String>();
        ArrayList<String> xpathNameList = new ArrayList<String>();
        Map<String, String> xpathDetails = new HashMap<String, String>();
        
        //System.out.println(elements);
        
        for (Element element : elements) {
            StringBuilder path;
            if (StringUtils.hasLength(element.id())) {
                path = new StringBuilder("//*[@id=\"" + element.id() + "\"]");
            } else {
                path = new StringBuilder(element.nodeName());
                int position = getElementPosition(element);
                if (position > 0) {
                    path.append("[").append(position).append("]");
                }
                for (Element el : element.parents()) {
                    if (StringUtils.hasLength(el.id())) {
                        path.insert(0, "//*[@id=\"" + el.id() + "\"]/");
                    } else {
                        position = getElementPosition(el);
                        if (position > 0) {
                            path.insert(0, el.nodeName() + "[" + position + "]/");
                        } else {
                            path.insert(0, el.nodeName() + '/');
                        }
                    }
                }
                int index = path.lastIndexOf("*");
                if (index > 3) {
                    path.delete(0, index - 2);
                }
            }
            //System.out.println(path + " = " + element.attributes());
            
            if(xpathList.size()==0) {
            	System.out.println();
            }
            String attributesNames="";
            String tempPath=""; 
            String elementname="";
            String elementOwnText = "";
            String elementGeneratedName = "";
            String nodeType = "";
            String node = "";
            StringBuffer elementNameBuffer = new StringBuffer("");
            Properties prop = new Properties();
            String propFileName = "nodeDetails.properties";
            InputStream inputStreamValue = getClass().getClassLoader().getResourceAsStream(propFileName);
			
            
            int elAttributesCount = element.attributes().size();
            if(!(elAttributesCount==0)) {
            	List<Attribute> attributeList = element.attributes().asList();
            	
            	String[] xpath = path.toString().split("/");
            	if(xpath[xpath.length-1].contains("[")) {
            		node = xpath[xpath.length-1].substring(0, xpath[xpath.length-1].indexOf("["));
            	}else {
            		node = xpath[xpath.length-1];
            	}
            	
            	for(int i=0; i<attributeList.size(); i++) {
            		String[] attributeDetails = attributeList.get(i).toString().split("=");
            		switch(attributeDetails[0]) {
            		case "href":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			
            			break;
            		case "name":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			break;
            		case "alt":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			break;
            		case "title":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			break;
            		case "placeholder":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			break;
            		case "src":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			break;
            		case "value":
            			elementNameBuffer.append(formattingOwnText(element, attributeDetails, prop, inputStreamValue, node));
            			break;
            		default:
            			tempPath = "";
            			break;
            		}
            	}
            	
            }
            
            String[] elementNameArr = elementNameBuffer.toString().split("_");
            
            if(!elementNameBuffer.toString().equals("") && elementNameArr.length>1) {
            	if(elementNameBuffer.substring(elementNameBuffer.length() - 1).equalsIgnoreCase("_")) {
            		tempPath = elementNameBuffer.toString().substring(0,(elementNameBuffer.length() - 1));
            	}
            	else tempPath=elementNameBuffer.toString();            	
            	
            	xpathList.add(path.toString());
            	String tmpPath1 = tempPath;
            	
//            	if(!xpathNameList.contains(tempPath)) {
            		xpathNameList.add(tempPath);
//            	}else {
//            		if()
//            	}
            	
//            		System.out.println(path.toString() +" elementname="+tmpPath1);
            	
            }
            
            elementNameBuffer = new StringBuffer("");
            
//            System.out.println(xpathList.size());
            
//            ExcelGenerate.createInnerExcel(path.toString(), element);
        }
        
        xpathDetails = makeUniqueName(xpathList, xpathNameList);
        
        ExcelGenerate.excelWrite(xpathList,xpathNameList, driver.getTitle());
//        ExcelGenerate.createInnerExcel(xpathAttributes, xpathOwnText);
        
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private String formattingOwnText(Element element, String[] attributeDetails, Properties prop, InputStream inputStreamValue, String node) throws Exception{
    	
    	String elementOwnText = "";
    	String nodeType = "";
    	String elementGeneratedName = "";
    	String elementname = "";
    	
    	if(!element.ownText().isEmpty()) {
			//elementOwnText = element.ownText().substring(0).replaceAll(" ", "_");
    		elementname = element.ownText().substring(0).replaceAll(" ", "_");
	}
    	
    	
    	else if(attributeDetails[0].equals("src")) {
    		String[] srcLink = attributeDetails[1].split("/");
    		if(srcLink[srcLink.length-1].contains(".")) {
    			elementname = srcLink[srcLink.length-1].substring(0, srcLink[srcLink.length-1].indexOf("."));
    		}else {
    			elementname = srcLink[srcLink.length-1];
    		}
    	}else {
    		elementname = attributeDetails[1].substring(1, attributeDetails[1].length()-1).replaceAll(" ", "_").replaceAll("/", "_");
    	}
    	
    	
    	
    	
    	prop.load(inputStreamValue);
		nodeType = prop.getProperty(node);
//		System.out.println(nodeType);
		if(nodeType==null) {
			nodeType = node;
//			System.out.println("true");
		}
		
		
//		elementGeneratedName = nodeType+"_"+elementOwnText+"_"+elementname;
		elementGeneratedName = nodeType+"_"+elementname;
		
    	
    	return elementGeneratedName;
    }
    
    
    private Map<String, String> makeUniqueName(ArrayList<String> xpathList, ArrayList<String> xpathNameList) {
    	
    	Map<String, String> xpathDetails = new HashMap<String, String>();
    	Map<String, Integer> tempDetails = new HashMap<String, Integer>();
    	for(int i=0; i<xpathNameList.size(); i++) {
    		int count = 1;
    		if(!xpathDetails.containsKey(xpathNameList.get(i))) {
    			xpathDetails.put(xpathNameList.get(i)+"_1", xpathList.get(i));
    			tempDetails.put(xpathNameList.get(i), count);
    		}else {
    			int lastNumber = tempDetails.get(xpathNameList.get(i));
    			xpathDetails.put(xpathNameList.get(i)+"_"+(lastNumber+1), xpathList.get(i));
    			tempDetails.replace(xpathNameList.get(i), lastNumber+1);
    			count = 1;
    		}
    	}
    	
    	
    	for(Map.Entry<String, String> pair : xpathDetails.entrySet()) {
    		System.out.println(pair.getValue()+"=>>>>>"+pair.getKey());
    	}
    	
    	
    	
    	
    	return xpathDetails;
    }
    
}