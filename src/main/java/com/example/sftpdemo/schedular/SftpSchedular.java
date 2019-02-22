package com.example.sftpdemo.schedular;

import java.util.Date;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.sftpdemo.config.SftpConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SftpSchedular {
	@Autowired
	private JSch jsch;
	@Autowired
	private SftpConfig sftpConfig;

	@Scheduled(cron = "${cronExpression}")
	public void execute() {

		Date fileLastModifiedDate = null;
		Date fileLastAccessDate = null;
		Session session = null;
		ChannelSftp sftpChannel = null;
		try {
			Date jobstartDate = new Date();
			log.info("Job started at " + jobstartDate);
			session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(sftpConfig.getPassword());
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			Vector<LsEntry> filesList = sftpChannel.ls(sftpConfig.getLocation());
			sftpChannel.cd(sftpConfig.getLocation());
			for (LsEntry sftpFile : filesList) {
				if (sftpFile.getAttrs().isDir()) {
					continue;
				}
				log.info("File Name - " + sftpFile.getFilename());
				fileLastModifiedDate = new Date(sftpFile.getAttrs().getMTime() * 1000L);
				log.info("Last Modified Time - " + fileLastModifiedDate);
				fileLastAccessDate = new Date(sftpFile.getAttrs().getATime() * 1000L);
				log.info("Last Accessed Time - " + fileLastAccessDate);
				if (fileLastAccessDate.before(jobstartDate)) {
					log.info("Downloading...");
					sftpChannel.get(sftpFile.getFilename(), sftpConfig.getLocalFileDownloadPath());
					log.info("Download Complete");
				}
			}
		} catch (Exception ex) {
			log.error("Unable to process due to Exception", ex);
		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}

		}
	}
}
