package test;

import hn.join.fieldwork.utils.XmlUtil;

import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="receipt")
public class XmlObject {
	
	@Element(name = "name")
	private String name;
	
	@ElementList(name = "item",required=false)
	private LinkedList<String> item = new LinkedList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public LinkedList<String> getItem() {
		return item;
	}

	public void setItem(LinkedList<String> item) {
		this.item = item;
	}

	public static void main(String[]args) throws Exception{
		XmlObject object=new XmlObject();
		object.setName("zzy");
//		object.getItem().add("item1");
//		object.getItem().add("item2");
		System.out.println(XmlUtil.toXml(object));
	}

}
