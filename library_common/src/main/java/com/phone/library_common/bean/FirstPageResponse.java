package com.phone.library_common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FirstPageResponse implements Parcelable {
	
	public  String     reason;
	public  ResultData result;
	private int        error_code;
	
	protected FirstPageResponse(Parcel in) {
		reason = in.readString();
		result = in.readParcelable(ResultData.class.getClassLoader());
		error_code = in.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(reason);
		dest.writeParcelable(result, flags);
		dest.writeInt(error_code);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<FirstPageResponse> CREATOR = new Creator<FirstPageResponse>() {
		@Override
		public FirstPageResponse createFromParcel(Parcel in) {
			return new FirstPageResponse(in);
		}
		
		@Override
		public FirstPageResponse[] newArray(int size) {
			return new FirstPageResponse[size];
		}
	};
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public ResultData getResult() {
		return result;
	}
	
	public void setResult(ResultData result) {
		this.result = result;
	}
	
	public int getError_code() {
		return error_code;
	}
	
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	
	public static class ResultData implements Parcelable {
		
		public String             stat;
		public List<JuheNewsBean> data;
		
		public String getStat() {
			return stat;
		}
		
		public void setStat(String stat) {
			this.stat = stat;
		}
		
		public List<JuheNewsBean> getData() {
			return data;
		}
		
		public void setData(List<JuheNewsBean> data) {
			this.data = data;
		}
		
		public static class JuheNewsBean implements Parcelable {
			
			public String uniquekey;
			public String title;
			public String date;
			public String category;
			public String author_name;
			public String url;
			public String thumbnail_pic_s;
			public String thumbnail_pic_s02;
			public String thumbnail_pic_s03;
			
			public String getUniquekey() {
				return uniquekey;
			}
			
			public void setUniquekey(String uniquekey) {
				this.uniquekey = uniquekey;
			}
			
			public String getTitle() {
				return title;
			}
			
			public void setTitle(String title) {
				this.title = title;
			}
			
			public String getDate() {
				return date;
			}
			
			public void setDate(String date) {
				this.date = date;
			}
			
			public String getCategory() {
				return category;
			}
			
			public void setCategory(String category) {
				this.category = category;
			}
			
			public String getAuthor_name() {
				return author_name;
			}
			
			public void setAuthor_name(String author_name) {
				this.author_name = author_name;
			}
			
			public String getUrl() {
				return url;
			}
			
			public void setUrl(String url) {
				this.url = url;
			}
			
			public String getThumbnail_pic_s() {
				return thumbnail_pic_s;
			}
			
			public void setThumbnail_pic_s(String thumbnail_pic_s) {
				this.thumbnail_pic_s = thumbnail_pic_s;
			}
			
			public String getThumbnail_pic_s02() {
				return thumbnail_pic_s02;
			}
			
			public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
				this.thumbnail_pic_s02 = thumbnail_pic_s02;
			}
			
			public String getThumbnail_pic_s03() {
				return thumbnail_pic_s03;
			}
			
			public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
				this.thumbnail_pic_s03 = thumbnail_pic_s03;
			}
			
			@Override
			public int describeContents() {
				return 0;
			}
			
			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(this.uniquekey);
				dest.writeString(this.title);
				dest.writeString(this.date);
				dest.writeString(this.category);
				dest.writeString(this.author_name);
				dest.writeString(this.url);
				dest.writeString(this.thumbnail_pic_s);
				dest.writeString(this.thumbnail_pic_s02);
				dest.writeString(this.thumbnail_pic_s03);
			}
			
			public void readFromParcel(Parcel source) {
				this.uniquekey = source.readString();
				this.title = source.readString();
				this.date = source.readString();
				this.category = source.readString();
				this.author_name = source.readString();
				this.url = source.readString();
				this.thumbnail_pic_s = source.readString();
				this.thumbnail_pic_s02 = source.readString();
				this.thumbnail_pic_s03 = source.readString();
			}
			
			public JuheNewsBean() {
			}
			
			protected JuheNewsBean(Parcel in) {
				this.uniquekey = in.readString();
				this.title = in.readString();
				this.date = in.readString();
				this.category = in.readString();
				this.author_name = in.readString();
				this.url = in.readString();
				this.thumbnail_pic_s = in.readString();
				this.thumbnail_pic_s02 = in.readString();
				this.thumbnail_pic_s03 = in.readString();
			}
			
			public static final Creator<JuheNewsBean> CREATOR = new Creator<JuheNewsBean>() {
				@Override
				public JuheNewsBean createFromParcel(Parcel source) {
					return new JuheNewsBean(source);
				}
				
				@Override
				public JuheNewsBean[] newArray(int size) {
					return new JuheNewsBean[size];
				}
			};

			@Override
			public String toString() {
				return "JuheNewsBean{" +
						"uniquekey='" + uniquekey + '\'' +
						", title='" + title + '\'' +
						", date='" + date + '\'' +
						", category='" + category + '\'' +
						", author_name='" + author_name + '\'' +
						", url='" + url + '\'' +
						", thumbnail_pic_s='" + thumbnail_pic_s + '\'' +
						", thumbnail_pic_s02='" + thumbnail_pic_s02 + '\'' +
						", thumbnail_pic_s03='" + thumbnail_pic_s03 + '\'' +
						'}';
			}
		}
		
		@Override
		public int describeContents() {
			return 0;
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.stat);
			dest.writeList(this.data);
		}
		
		public void readFromParcel(Parcel source) {
			this.stat = source.readString();
			this.data = new ArrayList<JuheNewsBean>();
			source.readList(this.data, JuheNewsBean.class.getClassLoader());
		}
		
		public ResultData() {
		}
		
		protected ResultData(Parcel in) {
			this.stat = in.readString();
			this.data = new ArrayList<JuheNewsBean>();
			in.readList(this.data, JuheNewsBean.class.getClassLoader());
		}
		
		public static final Creator<ResultData> CREATOR = new Creator<ResultData>() {
			@Override
			public ResultData createFromParcel(Parcel source) {
				return new ResultData(source);
			}
			
			@Override
			public ResultData[] newArray(int size) {
				return new ResultData[size];
			}
		};
	}
	
}
