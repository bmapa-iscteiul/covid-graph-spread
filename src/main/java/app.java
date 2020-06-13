package main.java;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class app {

	public static Git git;
	public static Document doc;
	
	public static void main(String[] args) {
		createHTMLTable();
		setHTMLTablesCSS();
		cloneRepository();
		getAllFilesFromTags();
		//createHTMLFile();
		System.out.println(cgi_lib.Header());
		Hashtable form_data = cgi_lib.ReadParse(System.in);
		
		System.out.println(doc.select("table"));
		System.out.println(doc.select("style"));
		
		System.out.println(cgi_lib.HtmlBot());
	}
	
	/**
	 * Lists all the tags and searches for the file associated 
	 */
	public static void getAllFilesFromTags() {
		Repository repository = git.getRepository();
		try {
			//gets the list of all tags
			List<Ref> call = git.tagList().call();
			
			for (Ref ref : call) {
				//System.out.println("Tag: " + ref.getName());
				RevWalk walk = new RevWalk(repository);
				try {
					RevObject object = walk.parseAny(ref.getObjectId());
					
					//gets the commit associated to the tag
					if (object instanceof RevCommit) {
						//System.out.println("RevCommit: " + ref.getName());
						//retrieves file from commit using the commit ObjectId
						getFileFromCommit(ref.getObjectId(), ref.getName());
					}
				} catch (Exception E) {
					
				}
			}
				
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * given a commit id and its tag, searches for covid19spreading.rdf file
	 * and if found, adds the commit to the table
	 * 
	 * @param commitId - the id of the commit
	 * @param tag - the tag associated to the commit
	 */
	public static void getFileFromCommit(ObjectId commitId, String tag) {
		Repository repository = git.getRepository();
	
		try(RevWalk revWalk = new RevWalk(repository)){
			RevCommit commit = revWalk.parseCommit(commitId);
			
			RevTree tree = commit.getTree();
			//System.out.println(commit.getId());
			//System.out.println("Tree: " + tree);
			try(TreeWalk treeWalk = new TreeWalk(repository)){
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));
				if(!treeWalk.next()) {
					throw new IllegalStateException("Didnt find file");
				}
				
				ObjectId objectId = treeWalk.getObjectId(0);
				//System.out.println(getHyperlinkOfFileFromCommit(commit));
				ObjectLoader loader = repository.open(objectId);
				addCommitToTable(commit, tag);
				//loader.copyTo(System.out);
			}
			revWalk.dispose();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Clones the remote repository localy to c:/path/to/repo
	 */
	public static void cloneRepository() {
		File f = new File("/path/to/repo");
		if (f.exists() && f.isDirectory()) {
			try {
				git = Git.open(new File("/path/to/repo/.git"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				git = Git.cloneRepository()
						.setURI("https://github.com/vbasto-iscte/ESII1920")
						.setDirectory(new File("/path/to/repo"))
						.call();
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * Creates the table 
	 */
	public static void createHTMLTable() {
		doc = Jsoup.parse("<html></html>");
		doc.body().addClass("body-styles-cls");
		doc.body().appendElement("div");
		doc.body().appendElement("table").attr("id", "t01");
		doc.body().appendElement("style");
		Element table = doc.select("table").get(0);
		table.append("<tr></tr>");
		Element headersRow = table.select("tr").get(0);
		headersRow.append("<th>File timestamp</th>");
		headersRow.append("<th>File name</th>");
		headersRow.append("<th>File tag</th>");
		headersRow.append("<th>Tag Description</th>");
		headersRow.append("<th>Spread Visualization Link</th>");
		//System.out.println(doc.toString());
		
		
	}
	
	
	/**
	 * @param tableIndex - the index of the table we want to add the row
	 * @param tableData - data array of the row
	 * 
	 * Given the table index, and the row data, adds a row to the table
	 */
	public static void addRowToHTMLTable(int tableIndex, String[] tableData) {
		Element table = doc.select("table").get(tableIndex);
		Element newRow = table.append("<tr></tr>");
		int newRowIndex = table.select("tr").size() - 1;
		for(int i =0; i < tableData.length; i++) {
			table.select("tr").get(newRowIndex).append("<td>" + tableData[i] + "</td>");
		}
		//System.out.println(doc.toString());
	}
	
	/**
	 * Css stylying of the table
	 */
	public static void setHTMLTablesCSS() {
		Element style = doc.select("style").get(0);
		style.append("table, th, td {border: 1px solid #add8e6; border-collapse: collapse;}");
		style.append("th, td {\r\n" + 
				"  padding: 15px;\r\n" + 
				"}");
		style.append("th {\r\n" + 
				"  text-align: left;\r\n" + 
				"}");
		style.append("table {\r\n" + 
				"  border-spacing: 5px;\r\n" + 
				"}");
		style.append("table#t01 tr:nth-child(even) {\r\n" + 
				"  background-color: #eee;\r\n" + 
				"}\r\n" + 
				"table#t01 tr:nth-child(odd) {\r\n" + 
				"  background-color: #fff;\r\n" + 
				"}\r\n" + 
				"table#t01 th {\r\n" + 
				"  color: black;\r\n" + 
				"  background-color: #add8e6;\r\n" + 
				"}");
		
		
	}
	
	/**@param commit - the commit we want to get the timestamp
	 * @return Returns the timestamp of a given commit
	 */
	public static String getCommitTimestamp(RevCommit commit) {
		PersonIdent authorIdent = commit.getAuthorIdent();
		Date authorDate = authorIdent.getWhen();
		TimeZone authorTimeZone = authorIdent.getTimeZone();
		return authorDate.toString();
	}
	
	/** @param commit - the commit
	 *  @return hyperlink - the concatenation of WebVOWL link + raw github file link
	 * Creates the spreading visualization link on WebVOWL concatenating the raw image hyperlink 
	 */
	public static String getHyperlinkOfFileFromCommit(RevCommit commit) {
		String commitId = commit.getName();
		String hyperlink = "http://www.visualdataweb.de/webvowl/#iri=https://raw.githubusercontent.com/vbasto-iscte/ESII1920/" + commitId + "/covid19spreading.rdf";
		return hyperlink;
	}
	
	/**@param commit - the commit
	 * @param tag - the tag associated to the commit
	 * 
	 * Creates a data array that contains each cell of the row 
	 * 1 - Tag timestamp 2 - Tag filename 3 - Tag message 4 - Tag description 5 - hyperlink to WebVOWL 
	 */
	public static void addCommitToTable(RevCommit commit, String tag) {
		String[] rowData = new String[5];
		rowData[0] = getCommitTimestamp(commit);
		rowData[1] = "covid19spreading.rdf";
		rowData[2] = tag.split("/",0)[2];
		rowData[3] = commit.getFullMessage();
		rowData[4] = "<a href='" + getHyperlinkOfFileFromCommit(commit) + "'>Link</a>";
		addRowToHTMLTable(0, rowData);
	}
}

