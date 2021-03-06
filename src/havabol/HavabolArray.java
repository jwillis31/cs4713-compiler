package havabol;

import java.util.ArrayList;

public class HavabolArray
{
	private ArrayList<ResultValue> array = new ArrayList<ResultValue>();
	public int dclType = -1;
	public int maxSize = 0;
	public int currentMaxIndex = -1;
	public boolean isUnbounded = false;
	public int elem = 0;
	
	public HavabolArray(int type)
	{
		this.dclType = type;
	}

	public void unsafeAppend(ResultValue resValToStore)
	{
		array.add(resValToStore);
		currentMaxIndex++;
		if(resValToStore != null)
		{
			elem = currentMaxIndex + 1;
		}
	}
	
	@Override
	public String toString()
	{
		String unboundedStrRep;
		if(isUnbounded)
		{
			unboundedStrRep = "T";
		}
		else
		{
			unboundedStrRep = "F";
		}
		String ret = "TYPE: " + Token.strSubClassifM[dclType] + ", " +
					 "MAXELEM: " + maxSize + ", " +
					 "ELEM: " + elem + ", " +
					 "UNBOUNDED: " + unboundedStrRep + " VALUES[";
		int count = 0;
		for(ResultValue val: array)
		{
			String stringToAppend = "";
			if(val == null)
			{
				stringToAppend = "";
			}
			else if(val.iDataType == Token.BOOLEAN)
			{
				if(((Boolean) val.value).booleanValue())
				{
					stringToAppend = "T";
				}
				else
				{
					stringToAppend = "F";
				}
			}
			else if(val.iDataType == Token.STRING)
			{
				stringToAppend = "\"" + ((StringBuilder) val.value).toString() + "\"";
			}
			else if(val.iDataType == Token.INTEGER ||
					val.iDataType == Token.FLOAT)
			{
				stringToAppend = ((Numeric) val.value).stringValue;
			}
			
			ret = ret + stringToAppend;
			
			if(count == this.currentMaxIndex)
			{
				ret = ret + "]";
			}
			else
			{
				ret = ret + ", ";
			}
			count++;
		}
		return ret;
	}
	
	public ResultValue getElem() throws Exception
	{
		return new ResultValue(
						Token.INTEGER,
						new Numeric(Integer.toString(this.elem), Token.INTEGER)
				   );
		
	}
	
	public ResultValue getMaxElem() throws Exception
	{
		return new ResultValue(
					Token.INTEGER,
					new Numeric(Integer.toString(this.maxSize), Token.INTEGER)
					);
	}
	
	public ResultValue getElement(int index)
	{
		if((index < 0) || (!isUnbounded && index >= this.maxSize))
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			return array.get(index);
		}
	}

	public void put(ResultValue valToStore, int index)
	{
		if((index < 0) || (!isUnbounded && index >= maxSize))
		{
			throw new IndexOutOfBoundsException();
		}
		else 
		{
			if(this.currentMaxIndex < index)
			{
				while(this.currentMaxIndex < index - 1)
				{
					currentMaxIndex++;
					array.add(null);
				}
				currentMaxIndex++;
				array.add(valToStore);
				elem = currentMaxIndex + 1;
			}
			else
			{
				array.set(index, valToStore);
			}
		}
	}

	public void setAll(ResultValue valToSet)
	{
		int index = 0;
		this.array = new ArrayList<ResultValue>();
		if(!this.isUnbounded)
		{
			while(index < maxSize)
			{
				this.array.add(valToSet);
				index++;
			}
			this.currentMaxIndex = index -1 ;
			this.elem = index;
		}
		else
		{
			while(index <= currentMaxIndex)
			{
				this.array.add(valToSet);
				index++;
			}
		}
	}

	public void fillWithArray(HavabolArray sourceArray) throws Exception
	{
		int index = 0;
		
		this.array = new ArrayList<ResultValue>();
		while(index < this.maxSize && index < sourceArray.maxSize && index < sourceArray.elem)
		{
			
			if(sourceArray.getElement(index) == null)
			{
				this.array.add(null);
			}
			else
			{
				ResultValue convertedValue = ResultValue.convertType(this.dclType, sourceArray.getElement(index));
				this.array.add(convertedValue);
			}
			index++;
		}
		this.currentMaxIndex = index - 1;
		this.elem = index;
	}
}
