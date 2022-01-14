/**
 * Trip File Drive Data VO
 * 
 * @author 넷케이티아이
 *
 */
public class TripDataVO  implements Cloneable
{
	private String strDayDist;
	private String strAccuDist;
	private String strDriveTime;
	private String strSpeed;
	private String strRpm;
	private String strBreak;
	private String strXcrd;
	private String strYcrd;
	private String strRd;
	private String strAccelVx;
	private String strAccelVy;
	private String strStatus;
	private String strFileName;
	private String strDriveData;
	private String strCarNo;
	private String strHndsetModel;

	public String getStrAccelVx()
	{
		return strAccelVx;
	}

	public void setStrAccelVx(String strAccelVx)
	{
		this.strAccelVx = strAccelVx;
	}

	public String getStrAccelVy()
	{
		return strAccelVy;
	}

	public void setStrAccelVy(String strAccelVy)
	{
		this.strAccelVy = strAccelVy;
	}

	public String getStrCarNo()
	{
		return strCarNo;
	}

	public void setStrCarNo(String strCarNo)
	{
		this.strCarNo = strCarNo;
	}

	public String getStrHndsetModel()
	{
		return strHndsetModel;
	}

	public void setStrHndsetModel(String strHndsetModel)
	{
		this.strHndsetModel = strHndsetModel;
	}

	public String getStrDriveData()
	{
		return strDriveData;
	}

	public void setStrDriveData(String strDriveData)
	{
		this.strDriveData = strDriveData;
	}

	public String getStrFileName()
	{
		return strFileName;
	}

	public void setStrFileName(String strFileName)
	{
		this.strFileName = strFileName;
	}

	public String getStrDayDist()
	{
		return strDayDist;
	}

	public void setStrDayDist(String strDayDist)
	{
		this.strDayDist = strDayDist;
	}

	public String getStrAccuDist()
	{
		return strAccuDist;
	}

	public void setStrAccuDist(String strAccuDist)
	{
		this.strAccuDist = strAccuDist;
	}

	public String getStrDriveTime()
	{
		return strDriveTime;
	}

	public void setStrDriveTime(String strDriveTime)
	{
		this.strDriveTime = strDriveTime;
	}

	public String getStrSpeed()
	{
		return strSpeed;
	}

	public void setStrSpeed(String strSpeed)
	{
		this.strSpeed = strSpeed;
	}

	public String getStrRpm()
	{
		return strRpm;
	}

	public void setStrRpm(String strRpm)
	{
		this.strRpm = strRpm;
	}

	public String getStrBreak()
	{
		return strBreak;
	}

	public void setStrBreak(String strBreak)
	{
		this.strBreak = strBreak;
	}

	public String getStrXcrd()
	{
		return strXcrd;
	}

	public void setStrXcrd(String strXcrd)
	{
		this.strXcrd = strXcrd;
	}

	public String getStrYcrd()
	{
		return strYcrd;
	}

	public void setStrYcrd(String strYcrd)
	{
		this.strYcrd = strYcrd;
	}

	public String getStrRd()
	{
		return strRd;
	}

	public void setStrRd(String strRd)
	{
		this.strRd = strRd;
	}

	public String getStrStatus()
	{
		return strStatus;
	}

	public void setStrStatus(String strStatus)
	{
		this.strStatus = strStatus;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException { // public 으로 바꿔주자.
		return super.clone();
	}

}
