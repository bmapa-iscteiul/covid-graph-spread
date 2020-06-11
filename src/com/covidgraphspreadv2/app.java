package com.covidgraphspreadv2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
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
		cloneRepository();
		getAllFilesFromTags();
		//createHTMLTable();
		//setHTMLTablesCSS();
		//String[] tableData = {"timestamp", "filename", "filetag", "filedescription", "link"};
		//addRowToHTMLTable(0,tableData);
		//createHTMLFile();
	}
	
	public static void getAllFilesFromTags() {
		Repository repository = git.getRepository();
		try {
			//gets the list of all tags
			List<Ref> call = git.tagList().call();
			
			for (Ref ref : call) {
				System.out.println("Tag: " + ref.getName());
				RevWalk walk = new RevWalk(repository);
				try {
					RevObject object = walk.parseAny(ref.getObjectId());
					
					//gets the commit associated to the tag
					if (object instanceof RevCommit) {
						System.out.println("RevCommit: " + ref.getName());
						//retrieves file from commit using the commit ObjectId
						getFileFromCommit(ref.getObjectId());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void getFileFromCommit(ObjectId commitId) {
		Repository repository = git.getRepository();
	
		try(RevWalk revWalk = new RevWalk(repository)){
			RevCommit commit = revWalk.parseCommit(commitId);
			
			RevTree tree = commit.getTree();
			System.out.println("Tree: " + tree);
			
			try(TreeWalk treeWalk = new TreeWalk(repository)){
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));
				if(!treeWalk.next()) {
					throw new IllegalStateException("Didnt find file");
				}
				
				ObjectId objectId = treeWalk.getObjectId(0);
				System.out.println(getHyperlinkOfFileFromCommit(commit));
				ObjectLoader loader = repository.open(objectId);
				String str = getCommitDescription(commit);
				//loader.copyTo(System.out);
			}
			revWalk.dispose();
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
			} catch (InvalidRemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
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
		headersRow.append("<th>Link</th>");
		//System.out.println(doc.toString());
	}
	
	public static void createHTMLFile() {
		File out = new File("covid-graph-spread.html");
	
		    String html = doc.toString();
		    BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(out));
				writer.write(html);
			    writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}
	
	public static void addRowToHTMLTable(int tableIndex, String[] tableData) {
		Element table = doc.select("table").get(tableIndex);
		Element newRow = table.append("<tr></tr>");
		int newRowIndex = table.select("tr").size() - 1;
		for(int i =0; i < tableData.length; i++) {
			table.select("tr").get(newRowIndex).append("<td>" + tableData[i] + "</td>");
		}
		System.out.println(doc.toString());
	}
	
	public static void setHTMLTablesCSS() {
		Element style = doc.select("style").get(0);
		style.append("table, th, td {border: 1px solid black; border-collapse: collapse;}");
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
	
	public static String getCommitTimestamp(RevCommit commit) {
		PersonIdent authorIdent = commit.getAuthorIdent();
		Date authorDate = authorIdent.getWhen();
		TimeZone authorTimeZone = authorIdent.getTimeZone();
		return authorDate.toString();
	}
	
	public static String getCommitDescription(RevCommit commit) {
		System.out.println("Descricao: " + commit.getFullMessage());
		return commit.getFullMessage();
	}
	
	public static String getHyperlinkOfFileFromCommit(RevCommit commit) {
		String commitId = commit.getName();
		String hyperlink = "https://github.com/vbasto-iscte/ESII1920/blob/" + commitId + "/covid19spreading.rdf";
		return hyperlink;
	}
}

