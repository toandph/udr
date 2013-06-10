package architectgroup.fact.dto;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class TraceDto implements Serializable
{
	/** 
	 * This attribute maps to the column traceid in the trace_build_1 table.
	 */
	protected int traceid;

	/** 
	 * This attribute maps to the column file in the trace_build_1 table.
	 */
	protected String file;

	/** 
	 * This attribute maps to the column method in the trace_build_1 table.
	 */
	protected String method;

	/** 
	 * This attribute maps to the column line in the trace_build_1 table.
	 */
	protected int line;

	/** 
	 * This attribute maps to the column text in the trace_build_1 table.
	 */
	protected String text;

	/** 
	 * This attribute maps to the column type in the trace_build_1 table.
	 */
	protected String type;

	/** 
	 * This attribute maps to the column refid in the trace_build_1 table.
	 */
	protected int refid;

	/** 
	 * This attribute maps to the column blockid in the trace_build_1 table.
	 */
	protected int blockid;

	/** 
	 * This attribute represents the foreign key relationship to the issue_build_1 table.
	 */
	protected IssueDto issue;

	/**
	 * Method 'TraceBuild1'
	 * 
	 */
	public TraceDto()
	{
	}

	/**
	 * Method 'getTraceid'
	 * 
	 * @return int
	 */
	public int getTraceid()
	{
		return traceid;
	}

	/**
	 * Method 'setTraceid'
	 * 
	 * @param traceid
	 */
	public void setTraceid(int traceid)
	{
		this.traceid = traceid;
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
	 * Method 'getText'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getText()
	{
		return text;
	}

	/**
	 * Method 'setText'
	 * 
	 * @param text
	 */
	public void setText(java.lang.String text)
	{
		this.text = text;
	}

	/**
	 * Method 'getType'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getType()
	{
		return type;
	}

	/**
	 * Method 'setType'
	 * 
	 * @param type
	 */
	public void setType(java.lang.String type)
	{
		this.type = type;
	}

	/**
	 * Method 'getRefid'
	 * 
	 * @return int
	 */
	public int getRefid()
	{
		return refid;
	}

	/**
	 * Method 'setRefid'
	 * 
	 * @param refid
	 */
	public void setRefid(int refid)
	{
		this.refid = refid;
	}

	/**
	 * Method 'getBlockid'
	 * 
	 * @return int
	 */
	public int getBlockid()
	{
		return blockid;
	}

	/**
	 * Method 'setBlockid'
	 * 
	 * @param blockid
	 */
	public void setBlockid(int blockid)
	{
		this.blockid = blockid;
	}

	/**
	 * Method 'getIssueBuild1'
	 * 
	 * @return IssueBuild1
	 */
	public IssueDto getIssue()
	{
		return issue;
	}

	/**
	 * Method 'setIssue'
	 * 
	 * @param issue
	 */
	public void setIssue(IssueDto issueBuild1)
	{
		this.issue = issueBuild1;
	}

}
