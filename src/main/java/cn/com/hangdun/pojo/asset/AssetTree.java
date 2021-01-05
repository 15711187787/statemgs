package cn.com.hangdun.pojo.asset;

public class AssetTree {
	  public static final int TREE_TYPE_ASSET_TYPE = 1;
	  
	  public static final int TREE_TYPE_ASSET_DOMAIN = 2;
	  
	  public static final int TREE_TYPE_ASSET_BUSINESS = 3;
	  
	  public static final int TREE_TYPE_ASSET_LOCATION = 4;
	  
	  public static final int TREE_TYPE_ASSET_ORG = 5;
	  
	  public static final int TREE_TYPE_ASSET_RESPONSIBLE = 6;
	  
	  private String id;
	  
	  private String code;
	  
	  private String name;
	  
	  private String icon;
	  
	  private String parentId;
	  
	  private AssetTree parent;
	  
	  private String idPath;
	  
	  private String namePath;
	  
	  private String description;
	  
	  private int treeType;
	  
	  private boolean isDelete;
	  
	  private String responsibleId;
	  
	  private String phone;
	  
	  private String webAttr;
	  
	  public String getId() {
	    return this.id;
	  }
	  
	  public void setId(String id) {
	    this.id = id;
	  }
	  
	  public String getName() {
	    return this.name;
	  }
	  
	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public String getIcon() {
	    return this.icon;
	  }
	  
	  public void setIcon(String icon) {
	    this.icon = icon;
	  }
	  
	  public String getParentId() {
	    return this.parentId;
	  }
	  
	  public void setParentId(String parentId) {
	    this.parentId = parentId;
	  }
	  
	  public String getIdPath() {
	    return this.idPath;
	  }
	  
	  public void setIdPath(String idPath) {
	    this.idPath = idPath;
	  }
	  
	  public String getNamePath() {
	    return this.namePath;
	  }
	  
	  public void setNamePath(String namePath) {
	    this.namePath = namePath;
	  }
	  
	  public String getDescription() {
	    return this.description;
	  }
	  
	  public void setDescription(String description) {
	    this.description = description;
	  }
	  
	  public int getTreeType() {
	    return this.treeType;
	  }
	  
	  public void setTreeType(int treeType) {
	    this.treeType = treeType;
	  }
	  
	  public String getCode() {
	    return this.code;
	  }
	  
	  public void setCode(String code) {
	    this.code = code;
	  }
	  
	  public AssetTree getParent() {
	    return this.parent;
	  }
	  
	  public void setParent(AssetTree parent) {
	    this.parent = parent;
	  }
	  
	  public boolean isIsDelete() {
	    return this.isDelete;
	  }
	  
	  public void setIsDelete(boolean delete) {
	    this.isDelete = delete;
	  }
	  
	  public String getResponsibleId() {
	    return this.responsibleId;
	  }
	  
	  public void setResponsibleId(String responsibleId) {
	    this.responsibleId = responsibleId;
	  }
	  
	  public String getPhone() {
	    return this.phone;
	  }
	  
	  public void setPhone(String phone) {
	    this.phone = phone;
	  }
	  
	  public String getWebAttr() {
	    return this.webAttr;
	  }
	  
	  public void setWebAttr(String webAttr) {
	    this.webAttr = webAttr;
	  }
	}
