package hn.join.fieldwork.domain;
/**
 * 产品对象
 * @author chenjinlong
 *
 */
public class Product extends BaseDomain{
	
	
	
	private static final long serialVersionUID = 3532481732538969933L;
	/**主键*/
	private Integer id;
	/**名称*/
	private String name;
	
	

	public Product(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	

	public Product() {
		super();
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
