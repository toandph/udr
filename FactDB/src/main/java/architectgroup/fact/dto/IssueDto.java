package architectgroup.fact.dto;

import architectgroup.fact.util.EnumUtil;
import architectgroup.fact.util.Status;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class IssueDto implements Serializable
{
	/** 
	 * This attribute maps to the column id in the issue_build_1 table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column file in the issue_build_1 table.
	 */
	protected String file;

	/** 
	 * This attribute maps to the column method in the issue_build_1 table.
	 */
	protected String method;

	/** 
	 * This attribute maps to the column line in the issue_build_1 table.
	 */
	protected int line;

	/** 
	 * This attribute maps to the column column in the issue_build_1 table.
	 */
	protected int column;

	/** 
	 * This attribute maps to the column message in the issue_build_1 table.
	 */
	protected String message;

	/** 
	 * This attribute maps to the column prefix in the issue_build_1 table.
	 */
	protected String prefix;

	/** 
	 * This attribute maps to the column postfix in the issue_build_1 table.
	 */
	protected String postfix;

	/** 
	 * This attribute maps to the column code in the issue_build_1 table.
	 */
	protected String code;

	/** 
	 * This attribute maps to the column severitylevel in the issue_build_1 table.
	 */
	protected short severitylevel;

	/** 
	 * This attribute maps to the column citingstatus in the issue_build_1 table.
	 */
	protected String citingstatus = Status.ANALYZE.toString();

	/** 
	 * This attribute maps to the column state in the issue_build_1 table.
	 */
	protected String state;

	/** 
	 * This attribute maps to the column display in the issue_build_1 table.
	 */
	protected String display;

    private List<TraceDto> traceList;

	/**
	 * Method 'IssueBuild1'
	 * 
	 */
	public IssueDto()
	{
	}

	/**
	 * Method 'getId'
	 * 
	 * @return int
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Method 'setId'
	 * 
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Method 'getFile'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFile()
	{
		return file;
	}

	/**
	 * Method 'setFile'
	 * 
	 * @param file
	 */
	public void setFile(java.lang.String file)
	{
		this.file = file;
	}

	/**
	 * Method 'getMethod'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMethod()
	{
		return method;
	}

	/**
	 * Method 'setMethod'
	 * 
	 * @param method
	 */
	public void setMethod(java.lang.String method)
	{
		this.method = method;
	}

	/**
	 * Method 'getLine'
	 * 
	 * @return int
	 */
	public int getLine()
	{
		return line;
	}

	/**
	 * Method 'setLine'
	 * 
	 * @param line
	 */
	public void setLine(int line)
	{
		this.line = line;
	}

	/**
	 * Method 'getColumn'
	 * 
	 * @return int
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * Method 'setColumn'
	 * 
	 * @param column
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * Method 'getMessage'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMessage()
	{
		return message;
	}

	/**
	 * Method 'setMessage'
	 * 
	 * @param message
	 */
	public void setMessage(java.lang.String message)
	{
		this.message = message;
	}

	/**
	 * Method 'getPrefix'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPrefix()
	{
		return prefix;
	}

	/**
	 * Method 'setPrefix'
	 * 
	 * @param prefix
	 */
	public void setPrefix(java.lang.String prefix)
	{
		this.prefix = prefix;
	}

	/**
	 * Method 'getPostfix'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPostfix()
	{
		return postfix;
	}

	/**
	 * Method 'setPostfix'
	 * 
	 * @param postfix
	 */
	public void setPostfix(java.lang.String postfix)
	{
		this.postfix = postfix;
	}

	/**
	 * Method 'getCode'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCode()
	{
		return code;
	}

	/**
	 * Method 'setCode'
	 * 
	 * @param code
	 */
	public void setCode(java.lang.String code)
	{
		this.code = code;
	}

	/**
	 * Method 'getSeveritylevel'
	 * 
	 * @return short
	 */
	public short getSeveritylevel()
	{
		return severitylevel;
	}

    public String getSeverity()
    {
        return EnumUtil.severityFromInt(severitylevel).getDescription();
    }

	/**
	 * Method 'setSeveritylevel'
	 * 
	 * @param severitylevel
	 */
	public void setSeveritylevel(short severitylevel)
	{
		this.severitylevel = severitylevel;
	}

	/**
	 * Method 'getCitingstatus'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCitingstatus()
	{
		return citingstatus;
	}

	/**
	 * Method 'setCitingstatus'
	 * 
	 * @param citingstatus
	 */
	public void setCitingstatus(java.lang.String citingstatus)
	{
		this.citingstatus = citingstatus;
	}

	/**
	 * Method 'getState'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getState()
	{
		return state;
	}

	/**
	 * Method 'setState'
	 * 
	 * @param state
	 */
	public void setState(java.lang.String state)
	{
		this.state = state;
	}

	/**
	 * Method 'getDisplay'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDisplay()
	{
		return display;
	}

	/**
	 * Method 'setDisplay'
	 * 
	 * @param display
	 */
	public void setDisplay(java.lang.String display)
	{
		this.display = display;
	}

    public List<TraceDto> getTraceList() {
        return traceList;
    }

    public void setTraceList(List<TraceDto> traceList) {
        this.traceList = traceList;
    }
}
