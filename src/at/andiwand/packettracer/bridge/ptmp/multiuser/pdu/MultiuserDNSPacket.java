package at.andiwand.packettracer.bridge.ptmp.multiuser.pdu;

import java.util.ArrayList;
import java.util.List;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.packetsocket.pdu.DNSPacket.DNSQuestion;
import at.andiwand.packetsocket.pdu.DNSPacket.DNSRessourceRecord;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;


public class MultiuserDNSPacket extends MultiuserPDU {
	
	/*
	 * CDnsHeader, 13827, false, 1, false, false, true, false, 0, 1, 0, 0,
	 * test.local, 4, 1, 86400, 0, -4713, 1, 1, 0, 0, 0, 0, false, 0, 0, 0
	 * 
	 * CDnsHeader, 16844, true, 1, false, false, true, false, 0, 1, 1, 0,
	 * test.local, 4, 1, 86400, 0, -4713, 1, 1, 0, 0, 0, 0, false, 1,
	 * test.local, 4, 1, 86400, 4, -4713, 1, 1, 0, 0, 0, 0, true, 192.168.15.101,
	 * 0, 0
	 * 
	 * CDnsHeader, 43966, true, 1, false, false, true, false, 0, 1, 2, 0,
	 * t.local, 4, 1, 86400, 0, -4713, 1, 1, 0, 0, 0, 0, false, 2,
	 * t.local, 0, 1, 86400, 10, -4713, 1, 1, 0, 0, 0, 0, true, true,
	 * test.local, 4, 1, 86400, 4, -4713, 1, 1, 0, 0, 0, 0, true, 192.168.15.101,
	 * 0, 0
	 * 
	 * header
	 * 		CDnsHeader, 16844, true, 1, false, false, true, false, 0, 1, 1, 0
	 * entry
	 * 		test.local, 4, 1, 86400, 0, -4713, 1, 1, 0, 0, 0, 0, false, 1
	 * trailing
	 * 		0, 0
	 * 
	 * header
	 * 		CDnsHeader, id, response, operation code, aa, tc,
	 * 		rd, ra, aa count, tc count, rd count, ra count
	 * entry
	 * 		domain name, type?, class, time to live, data length?,
	 * 		?, 1, 1, 0, 0, 0, 0, payload y/n?, payload
	 * trailing
	 * 		0, 0
	 */
	
	public static class DNSEntry {
		private String domainName;
		private byte type;
		private byte clazz;
		private int timeToLive;
		private IPv4Address ressource;
		
		public String getDomainName() {
			return domainName;
		}
		
		public byte getType() {
			return type;
		}
		
		public byte getClazz() {
			return clazz;
		}
		
		public int getTimeToLive() {
			return timeToLive;
		}
		
		public IPv4Address getRessource() {
			return ressource;
		}
		
		public void setDomainName(String domainName) {
			this.domainName = domainName;
		}
		
		public void setType(byte type) {
			this.type = type;
		}
		
		public void setClazz(byte clazz) {
			this.clazz = clazz;
		}
		
		public void setTimeToLive(int timeToLive) {
			this.timeToLive = timeToLive;
		}
		
		public void setRessource(IPv4Address ressource) {
			this.ressource = ressource;
		}
	}
	
	private short id;
	private boolean response;
	private byte operationCode;
	private boolean authoritativeAnswer;
	private boolean trunCation;
	private boolean recursionDesired;
	private boolean recursionAvailable;
	private List<DNSEntry> questions = new ArrayList<DNSEntry>();
	private List<DNSEntry> answers = new ArrayList<DNSEntry>();
	private List<DNSEntry> nameServers = new ArrayList<DNSEntry>();
	private List<DNSEntry> additionalRecords = new ArrayList<DNSEntry>();
	
	public short getId() {
		return id;
	}
	
	public boolean isResponse() {
		return response;
	}
	
	public byte getOperationCode() {
		return operationCode;
	}
	
	public boolean isAuthoritativeAnswer() {
		return authoritativeAnswer;
	}
	
	public boolean isTrunCation() {
		return trunCation;
	}
	
	public boolean isRecursionDesired() {
		return recursionDesired;
	}
	
	public boolean isRecursionAvailable() {
		return recursionAvailable;
	}
	
	public List<DNSEntry> getQuestions() {
		return new ArrayList<DNSEntry>(questions);
	}
	
	public List<DNSEntry> getAnswers() {
		return new ArrayList<DNSEntry>(answers);
	}
	
	public List<DNSEntry> getNameServers() {
		return new ArrayList<DNSEntry>(nameServers);
	}
	
	public List<DNSEntry> getAdditionalRecords() {
		return new ArrayList<DNSEntry>(additionalRecords);
	}
	
	@Override
	public void getBytes(PTMPDataWriter writer) {
		writer.writeString("CDnsHeader");
		writer.writeShort(id);
		writer.writeBoolean(response);
		writer.writeByte(operationCode);
		writer.writeBoolean(authoritativeAnswer);
		writer.writeBoolean(trunCation);
		writer.writeBoolean(recursionDesired);
		writer.writeBoolean(recursionAvailable);
		writer.writeShort(getQuestionCount());
		writer.writeShort(getAnswerCount());
		writer.writeShort(getNameServerCount());
		writer.writeShort(getAdditionalRecordCount());
		
		List<List<DNSEntry>> recordsLists = new ArrayList<List<DNSEntry>>();
		recordsLists.add(getQuestions());
		recordsLists.add(getAnswers());
		recordsLists.add(getNameServers());
		recordsLists.add(getAdditionalRecords());
		
		// TODO: rename
		for (List<DNSEntry> records : recordsLists) {
			for (DNSEntry record : records) {
				record.toString();
			}
		}
		
		writer.writeInt(0);
		writer.writeInt(0);
	}
	
	public void setId(short id) {
		this.id = id;
	}
	
	public void setResponse(boolean response) {
		this.response = response;
	}
	
	public void setOperationCode(byte operationCode) {
		this.operationCode = operationCode;
	}
	
	public void setAuthoritativeAnswer(boolean authoritativeAnswer) {
		this.authoritativeAnswer = authoritativeAnswer;
	}
	
	public void setTrunCation(boolean trunCation) {
		this.trunCation = trunCation;
	}
	
	public void setRecursionDesired(boolean recursionDesired) {
		this.recursionDesired = recursionDesired;
	}
	
	public void setRecursionAvailable(boolean recursionAvailable) {
		this.recursionAvailable = recursionAvailable;
	}
	
	public int getQuestionCount() {
		return questions.size();
	}
	
	public int getAnswerCount() {
		return answers.size();
	}
	
	public int getNameServerCount() {
		return nameServers.size();
	}
	
	public int getAdditionalRecordCount() {
		return additionalRecords.size();
	}
	
	public void setQuestions(List<DNSEntry> questions) {
		this.questions = new ArrayList<DNSEntry>(questions);
	}
	
	public void setAnswers(List<DNSEntry> answers) {
		this.answers = new ArrayList<DNSEntry>(answers);
	}
	
	public void setNameServers(List<DNSEntry> nameServers) {
		this.nameServers = new ArrayList<DNSEntry>(nameServers);
	}
	
	public void setAdditionalRecords(List<DNSEntry> additionalRecords) {
		this.additionalRecords = new ArrayList<DNSEntry>(additionalRecords);
	}
	
	public void addQuestion(DNSEntry question) {
		questions.add(question);
	}
	
	public void addAnswer(DNSEntry answer) {
		answers.add(answer);
	}
	
	public void addNameServer(DNSEntry nameServer) {
		nameServers.add(nameServer);
	}
	
	public void addAdditionalRecord(DNSEntry additionalRecord) {
		additionalRecords.add(additionalRecord);
	}
	
	public void removeQuestion(DNSQuestion question) {
		questions.remove(question);
	}
	
	public void removeAnswer(DNSRessourceRecord answer) {
		answers.remove(answer);
	}
	
	public void removeNameServer(DNSRessourceRecord nameServer) {
		nameServers.remove(nameServer);
	}
	
	public void removeAdditionalRecord(DNSRessourceRecord additionalRecords) {
		questions.remove(additionalRecords);
	}
	
	@Override
	public void parse(PTMPDataReader reader) {
		reader.readString();
		id = (short) (reader.readInt() & 0xffff);
		response = reader.readBoolean();
		operationCode = reader.readByte();
		authoritativeAnswer = reader.readBoolean();
		trunCation = reader.readBoolean();
		recursionDesired = reader.readBoolean();
		recursionAvailable = reader.readBoolean();
		int questionCount = reader.readShort();
		int answerCount = reader.readShort();
		int nameServerCount = reader.readShort();
		int additionalRecordCount = reader.readShort();
		
		int answerEnd = questionCount + answerCount;
		int nameServerEnd = answerEnd + nameServerCount;
		int recordCount = nameServerEnd + additionalRecordCount;
		
		for (int i = 0; i < recordCount; i++) {
			DNSEntry entry = new DNSEntry();
			
			entry.setDomainName(reader.readString());
			entry.setType(reader.readByte());
			entry.setClazz(reader.readByte());
			entry.setTimeToLive(reader.readInt());
			reader.readInt();
			reader.readInt();
			reader.readInt();
			reader.readInt();
			reader.readInt();
			reader.readInt();
			reader.readInt();
			reader.readInt();
			reader.readBoolean();
			reader.readString();
			
			if (i < questionCount) addAnswer(entry);
			else if (i < answerEnd) addNameServer(entry);
			else if (i < nameServerEnd) addNameServer(entry);
			else addAdditionalRecord(entry);
		}
		
		reader.readInt();
		reader.readInt();
	}
	
}