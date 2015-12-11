package util.mongo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import dbmodels.Publication;

public class MongoDBConnectMacro {
	public static abstract class Looper {
		public MongoClient cli = null;
		public DB db = null;
		public DBCollection coll = null;
		public String collName = null;

		public abstract void handle(DBObject obj);
	}

	public static abstract class LooperWithData<T> extends Looper {
		protected T data = null;

		public LooperWithData() {
		}

		public LooperWithData(T data) {
			this.data = data;
		}

	}

	Logger logger = Logger.getLogger(getClass());

	private String MONGODB_HOST = "localhost";
	private Integer MONGODB_PORT = 27017;
	private String MONGODB_DBNAME = "admin";
	private boolean MONGODB_AUTH = false;
	private String MONGODB_AUTH_USERNAME = "root";
	private String MONGODB_AUTH_PASSWORD = "root";

	private static Integer DEFAULT_WRITE_BATCH_WINDOW = 100;
	private static Integer DEFAULT_RETRY_TIMES = 1000;

	private static Integer DEFAULT_LOG_INTERVAL = 10000;

	MongoClient mongoClient = null;
	DB db = null;

	/**
	 * init
	 * 
	 * @return mongo client
	 */
	public MongoClient init() {
		return this.mongoInitConnection();
	}

	/**
	 * check if the connection is connected
	 * 
	 * @return
	 */
	public boolean check() {
		return this.mongoIsConnected();
	}

	/**
	 * close db
	 */
	public void close() {
		this.mongoDisconnect();
	}

	/**
	 * get client
	 * 
	 * @return
	 */
	public MongoClient client() {
		return getMongoClient();
	}

	/**
	 * get db
	 * 
	 * @param name
	 *            db name
	 * @return
	 */
	public DB db(String name) {
		return getDb(name);
	}

	/**
	 * get default db (configured in config.properties)
	 * 
	 * @return
	 */
	public DB db() {
		return getDefaultDb();
	}

	/**
	 * get collection
	 * 
	 * @param name
	 * @return
	 */
	public DBCollection coll(String name) {
		return getCollection(name);
	}

	/**
	 * get count of a collection
	 * 
	 * @param name
	 *            collection name
	 * @return
	 */
	public Long count(String name) {
		return getCollectCount(name);
	}

	private boolean mongoLoadConfig() {
		String fileName = "myConfig.properties";
		Properties properties = new Properties();
		FileInputStream fileInpustStream = null;
		boolean status = false;
		try {
			fileInpustStream = new FileInputStream(fileName);
			properties.load(fileInpustStream);
			fileInpustStream.close();
		//	properties.list(System.out);
			// mongo.db.host=159.226.244.58
			// public static String MONGODB_HOST = "localhost";
			if (properties.containsKey("mongo.db.host")) {
				MONGODB_HOST = properties.getProperty("mongo.db.host");
			} else {
			}
			// mongo.db.port=27017
			// public static Integer MONGODB_PORT = 27017;
			if (properties.containsKey("mongo.db.port")) {
				MONGODB_PORT = Integer.parseInt(properties
						.getProperty("mongo.db.port"));
			} else {
			}
			// mongo.db.name=nsfc
			// public static String MONGODB_DBNAME = "admin";
			if (properties.containsKey("mongo.db.name")) {
				MONGODB_DBNAME = properties.getProperty("mongo.db.name");
			} else {
			}
			// mongo.db.auth=false
			// public static boolean MONGODB_AUTH = false;
			if (properties.containsKey("mongo.db.auth")) {
				MONGODB_AUTH = properties.getProperty("mongo.db.auth").equals(
						"1")
						|| properties.getProperty("mongo.db.auth")
								.toLowerCase().equals("true");
			} else {
			}
			if (MONGODB_AUTH) {
				// mongo.db.auth.username=kegger_bigsci
				// public static String MONGODB_AUTH_USERNAME = "admin";
				if (properties.containsKey("mongo.db.auth.username")) {
					MONGODB_AUTH_USERNAME = properties
							.getProperty("mongo.db.auth.username");
				} else {
				}
				// public static String MONGODB_AUTH_PASSWORD = "admin";
				// mongo.db.auth.password=datiantian123!@#
				if (properties.containsKey("mongo.db.auth.password")) {
					MONGODB_AUTH_PASSWORD = properties
							.getProperty("mongo.db.auth.password");
				} else {
				}
			}

			status = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		if (status) {
			configFileIsLoaded = true;
		}
		return status;
	}

	boolean configFileIsLoaded = false;

	
	@SuppressWarnings("deprecation")
	public MongoClient mongoInitConnection() {
		mongoDisconnect();
		if (!configFileIsLoaded) {
			logger.info("load config file for mongodb macro ...");
			if (!mongoLoadConfig()) {
				logger.info("read config file failed ... init connection terminted ");
				return null;
			}
		}
		int i=0;
		do {
			try {
				if (MONGODB_AUTH) {
					// logger.info("auth" + "##" + MONGODB_AUTH_USERNAME + "##"
					// + MONGODB_DBNAME + "##" + MONGODB_AUTH_PASSWORD);
					MongoCredential credential = MongoCredential
							.createMongoCRCredential(MONGODB_AUTH_USERNAME,
									MONGODB_DBNAME,
									MONGODB_AUTH_PASSWORD.toCharArray());
					// logger.info(" host :" + MONGODB_HOST + " ## port " +
					// MONGODB_PORT);
					mongoClient = new MongoClient(new ServerAddress(
							MONGODB_HOST, MONGODB_PORT),
							Arrays.asList(credential));

					// List<ServerAddress> seeds = new
					// ArrayList<ServerAddress>();
					// seeds.add(new ServerAddress(MONGODB_HOST, MONGODB_PORT));
					// mongoClient = new MongoClient(seeds,
					// Arrays.asList(credential));
					// mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
				} else {
					// logger.info("not authorized ... ");
					mongoClient = new MongoClient(MONGODB_HOST, MONGODB_PORT);
				}

				db = mongoClient.getDB(MONGODB_DBNAME);
				Set<String> collectionNames = db.getCollectionNames();
				logger.info("connected ...  collections : " + collectionNames);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("retrying ... ");
				try {
					Thread.sleep(3333);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				i++;
				if(i>3)
				{
					break;
				}
				continue;
				
			}
			break;
		} while (true);
		logger.info("connection inited");
		return mongoClient;
	}

	public boolean mongoIsConnected() {
		try {
			@SuppressWarnings("deprecation")
			DB mdb = mongoClient.getDB(MONGODB_DBNAME);
			mdb.getCollectionNames();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void mongoDisconnect() {
		try {
			if (mongoClient != null) {
				logger.info("try disconnect mongo connection ");
				mongoClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("dossconect failed , sth err ~ ? ");
		} finally {
			mongoClient = null;
			logger.info("mongo disconnected ");
		}
	}

	public boolean insertBatch(String coll, List<DBObject> data) {
		return insertBatch(getCollection(coll), data);
	}

	public boolean insertBatch(String coll, List<DBObject> data, int windowSize) {
		return insertBatch(getCollection(coll), data, windowSize);
	}

	public boolean insertBatch(String coll, List<DBObject> data,
			int windowSize, int retryTimes) {
		return insertBatch(getCollection(coll), data, windowSize, retryTimes);
	}

	public boolean insertBatch(DBCollection coll, List<DBObject> data) {
		return insertBatch(coll, data, DEFAULT_WRITE_BATCH_WINDOW,
				DEFAULT_RETRY_TIMES);
	}

	public boolean insertBatch(DBCollection coll, List<DBObject> data,
			int windowSize) {
		return insertBatch(coll, data, windowSize, DEFAULT_RETRY_TIMES);
	}

	public boolean insertBatch(DBCollection coll, List<DBObject> data,
			int windowSize, int retryTimes) {
		String collName = null;
		try {
			collName = coll.getName();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		int pSize = data.size();
		int alsoRetriedTimes = 0;
		boolean status = true;
		for (int off = 0; off < pSize; off += windowSize) {
			while (true) {
				try {
					List<DBObject> subPubs = data.subList(off,
							off + windowSize <= pSize ? off + windowSize
									: pSize);
					coll.insert(subPubs);
				} catch (Exception e1) {
					alsoRetriedTimes++;
					if (retryTimes > 0 && alsoRetriedTimes > retryTimes) {
						status = false;
						break;
					}
					e1.printStackTrace();
					try {
						db.getCollectionNames();
						coll = getCollection(collName);
					} catch (Exception e3) {
						this.mongoInitConnection();
						continue;
					}
					System.err.println("not the problem of connections");
				}
				break;
			}
		}

		return status;
	}

	public boolean removeBatchByStringId(String collName, Set<String> idSet) {
		List<ObjectId> idList = new ArrayList<ObjectId>();
		for (String id : idSet) {
			idList.add(new ObjectId(id));
		}
		return removeBatchById(collName, idList);
	}

	public boolean removeBatchByStringId(String collName, List<String> idSet) {
		List<ObjectId> idList = new ArrayList<ObjectId>();
		for (String id : idSet) {
			idList.add(new ObjectId(id));
		}
		return removeBatchById(collName, idList);
	}

	public boolean removeBatchById(String collName, Set<ObjectId> idSet) {
		List<ObjectId> idList = new ArrayList<ObjectId>();
		idList.addAll(idSet);
		return removeBatchById(collName, idList);
	}

	public boolean removeBatchById(String collName, List<ObjectId> idList) {
		DBCollection coll = getCollection(collName);
		int windowSize = 1000;
		int pSize = idList.size();
		for (int off = 0; off < pSize; off += windowSize) {
			while (true) {
				try {
					List<ObjectId> subPubs = idList.subList(off, off
							+ windowSize <= pSize ? off + windowSize : pSize);
					DBObject obj = new BasicDBObject();
					obj.put("$in", subPubs);
					DBObject query = new BasicDBObject();
					query.put("_id", obj);
					coll.remove(query);

					// DBCursor find = coll.find(query);
					// while (find.hasNext()) {
					// logger.info("remove:" + find.next().get("_id"));
					// }
					// logger.info("~~~~~~~~~~~~~~~~~~");
				} catch (Exception e1) {

					e1.printStackTrace();
					try {
						db.getCollectionNames();
						coll = getCollection(collName);
					} catch (Exception e3) {
						this.mongoInitConnection();
					}
					System.err.println("not the problem of connections");
					continue;
				}
				break;
			}
		}
		return false;
	}

	/**
	 * get mongo client
	 * 
	 * @return
	 */
	public MongoClient getMongoClient() {
		if (mongoClient == null) {
			mongoInitConnection();
		}
		return mongoClient;
	}

	public DB getDefaultDb() {
		if (db == null) {
			mongoInitConnection();
		}
		return db;
	}

	@SuppressWarnings("deprecation")
	public DB getDb(String dbName) {
		MongoClient mongoClient = getMongoClient();
		return mongoClient.getDB(dbName);
	}

	public DBCollection getCollection(String name) {
		return this.getDefaultDb().getCollection(name);
	}

	public Long getCollectCount(String collName) {
		return getCollectCount(getCollection(collName));
	}

	public static Long getCollectCount(DBCollection coll) {
		return coll.getStats().getLong("count");
	}
	
	  /**
     *根据表名查询,并按ts进行排序，返回num条
     * @return  pubidlist
     */
	@SuppressWarnings("static-access")
	public ArrayList<Publication> getPubList(String tablename,int num,String query)
	{
		ArrayList<Publication> publist = new ArrayList<Publication>();
		try{
			Cursor cur=getCollection(tablename).find().sort(new BasicDBObject(query,-1)).limit(num);
			Publication pub=new Publication();
			while(cur.hasNext())
			{
				DBObject obj=cur.next();
		    	//反转
				pub=pub.fromMongdbObject(obj);
				publist.add(pub);
			}
		} catch (MongoException e) {
			logger.info("select error!");
			e.printStackTrace();
		} finally {
		}
		return publist;
	}

	//插入论文信息
	public int insertPublictionBeanInfo( Publication pub,String websource){
		DBCollection coll = db.getCollection("publication_"+websource);
		try {					
			coll.insert(pub.toMongodbObject());	
			//coll.insert((DBObject)pu);	
			return 1;
		}catch (MongoException e) {
			logger.info("insert error!");
			e.printStackTrace();
		}
		return 0;
	}
	

	public void loop(String collName, Looper looper) {
		loop(collName, null, null, true, "..", looper);
	}

	public void loop(String collName, String message, Looper looper) {
		loop(collName, null, null, true, message, looper);
	}

	public void loop(String collName, DBObject query, String message,
			Looper looper) {
		loop(collName, query, null, true, message, looper);
	}

	public void loop(String collName, DBObject query, boolean checkAllSize,
			String message, Looper looper) {
		loop(collName, query, null, checkAllSize, message, looper);
	}

	public void loop(String collName, DBObject query, DBObject fields,
			boolean checkAllSize, String message, Looper looper) {
		DBCollection coll = db.getCollection(collName);
		int offset = 0;
		DBCursor cursor = null;
		Long allSize = (long) -1;
		long allTimeStart = System.currentTimeMillis();
		// init lopper's para
		// public MongoClient cli = null;
		// public DB db = null;
		// public DBCollection coll = null;
		// public String collName = null;
		looper.cli = mongoClient;
		looper.db = db;
		looper.coll = coll;
		looper.collName = collName;
		while (true) {
			try {
				coll = db.getCollection(collName);
				if (query == null) {
					if (fields == null) {
						cursor = coll.find();
					} else {
						cursor = coll.find(query, fields); // How to handle this
															// ?
					}
				} else {
					if (fields == null) { // query is not null , and field is
											// null
						cursor = coll.find(query);
					} else { // query is not null ,and field is not null
						cursor = coll.find(query, fields);
					}
				}

				if (offset == 0) {
					if (checkAllSize) {
						if (query == null) {
							allSize = getCollectCount(coll);
						} else {
							allSize = (long) cursor.length();
						}
						if (allSize < 1) {
							allSize = (long) 1;
						}
						logger.info("all size :" + allSize);
					}
				} else {
					cursor.skip(offset);
				}
				// cursor =
				// cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT).batchSize(DEFAULT_LOG_INTERVAL).snapshot();
				cursor = cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT)
						.snapshot();
				// ref :
				// https://groups.google.com/forum/#!topic/mongodb-user/iCRt_9aN0Rc
				// chain:
				// http://api.mongodb.org/java/2.1/com/mongodb/DBCursor.html#addOption%28int%29
				// chain: http://api.mongodb.org/java/2.1/com/mongodb/Bytes.html
				while (cursor.hasNext()) {
					looper.handle(cursor.next());
					offset++;
					if (offset % DEFAULT_LOG_INTERVAL == 0) {
						if (checkAllSize) {
							logger.info(message
									+ "["
									+ collName
									+ "] ["
									+ offset
									+ "] of all : ["
									+ allSize
									+ //
									"] time also cost :"
									+ (System.currentTimeMillis() - allTimeStart) //
									+ " ms , ( "
									+ String.format("%.2f",
											100 * ((double) (offset) / allSize))
									+ " % )");
						} else {
							logger.info(message
									+ "["
									+ collName
									+ "] ["
									+ offset
									+ //
									"] time also cost :"
									+ (System.currentTimeMillis() - allTimeStart)
									+ " ms");
						}

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(333);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.mongoInitConnection();
				coll = db.getCollection(collName);
				looper.cli = mongoClient;
				looper.db = db;
				looper.coll = coll;
				looper.collName = collName;
				continue;
			}
			break;
		}
	}

	// MongoDBConnectMacro mongodbMacro = new MongoDBConnectMacro();
	// mongodbMacro.mongoInitConnection();
	// boolean checkMongoIsConnected = mongodbMacro.mongoIsConnected();
	// mongodbMacro.mongoDisconnect();

}
