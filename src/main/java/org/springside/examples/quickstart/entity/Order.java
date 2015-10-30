package org.springside.examples.quickstart.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_order")
public class Order extends IdEntity {
	
	
	private Long userId;
	
	
	@Column(name="user_id")
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}


	private Long osId;
	

	private Long productId;
	

	private String productName;
	
	private Integer type;//订单类型 1-普通订单 2-充值订单 3-预约订单 4-后台添加订单
	
	
	private   String phone;
	
	private  String userName;
	
	@Transient
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	private Double money;
	private BigDecimal price;
	private Integer num;
	private Integer status;
	

	private String orderNo;
	

	private String consumeCode;
	

	private String sftOrderNo;
	

	private String sessionToken;
	

	private Date bookTime;
	

	private Date createTime;
	
	
	private Long  couponId;
	
	
/*	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<CouponList> couponList = new ArrayList<CouponList>();*/

	@Column(name="consume_code")
	public String getConsumeCode() {
		return consumeCode;
	}

	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}

	@Column(name="get_coupon_id")
	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Order() {

	}

	public Order(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name="book_time")
	public Date getBookTime() {
		return bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}

	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "os_id")
//	public OilStation getOilStation() {
//		return oilStation;
//	}
//
//	public void setOilStation(OilStation oilStation) {
//		this.oilStation = oilStation;
//	}

/*	public List<CouponList> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponList> couponList) {
		this.couponList = couponList;
	}*/
	@Column(name="os_id")
	public Long getOsId() {
		return osId;
	}

	public void setOsId(Long osId) {
		this.osId = osId;
	}

	@Column(name="product_id")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name="product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name="order_no")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name="sft_order_no")
	public String getSftOrderNo() {
		return sftOrderNo;
	}

	public void setSftOrderNo(String sftOrderNo) {
		this.sftOrderNo = sftOrderNo;
	}

	@Column(name="session_token")
	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
