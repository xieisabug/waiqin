import hn.join.fieldwork.utils.XmlUtil;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "item")
public class SimpleItem {
	
	@Element(name = "name")
	private String itemName;
	
	@Element(name = "description",required=false)
	private String description;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static void main(String []args) throws Exception{
		SimpleItem item=new SimpleItem();
		item.setItemName("xbox-360");
//		item.setDescription("Gaming,MicroSoft");
		System.out.println(XmlUtil.toXml(item));
	}
}
