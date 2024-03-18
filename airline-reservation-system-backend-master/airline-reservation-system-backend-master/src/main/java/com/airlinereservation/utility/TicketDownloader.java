package com.airlinereservation.utility;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.airlinereservation.entity.FlightBooking;
import com.airlinereservation.utility.Constants.FlightClassType;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

import jakarta.servlet.http.HttpServletResponse;

public class TicketDownloader {
	
	private List<FlightBooking> bookings;
	
	public TicketDownloader() {
		
	}

	public TicketDownloader(List<FlightBooking> bookings) {
		super();
		this.bookings = bookings;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new Color(237, 242, 243));
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(new Color(31, 53, 65));

		cell.setPhrase(new Phrase("Flight Seat", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Price", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Booing Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Status", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		for (FlightBooking booking : bookings) {
			table.addCell(booking.getAirplaneSeatNo().getSeatNo());
			
			if(booking.getFlightClass().equals(FlightClassType.ECONOMY.value())) {
				table.addCell(String.valueOf(booking.getFlight().getEconomySeatFare()));
			} else if(booking.getFlightClass().equals(FlightClassType.BUSINESS.value())) {
				table.addCell(String.valueOf(booking.getFlight().getBusinessSeatFare()));
			} else if(booking.getFlightClass().equals(FlightClassType.FIRST_CLASS.value())) {
				table.addCell(String.valueOf(booking.getFlight().getFirstClassSeatFare()));
			}
		
			table.addCell(DateTimeUtils.getProperDateTimeFormatFromEpochTime(booking.getBookingTime()));
			table.addCell(booking.getStatus());
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Image image = Image.getInstance("classpath:images/logo.png");
		image.scalePercent(8.0f, 8.0f);
		document.add(image);

		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		headerFont.setSize(25);
		headerFont.setColor(new Color(31, 53, 65));
		Paragraph pHeader = new Paragraph("Flight Ticket Details\n", headerFont);
		pHeader.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(pHeader);

		Font fontD = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontD.setSize(13);
		fontD.setColor(Color.BLACK);
		Paragraph pD = new Paragraph("Flight Number: " + bookings.get(0).getFlight().getFlightNumber(), fontD);
		pD.setAlignment(Paragraph.ALIGN_RIGHT);
		document.add(pD);

		Font fontAI = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontAI.setSize(13);
		fontAI.setColor(Color.BLACK);
		Paragraph pAI = new Paragraph("Customer Booking Id: " + bookings.get(0).getBookingId(), fontAI);
		pAI.setAlignment(Paragraph.ALIGN_RIGHT);
		document.add(pAI);

		Font fontP = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontP.setSize(18);
		fontP.setColor(new Color(31, 53, 65));
		Chunk glue = new Chunk(new VerticalPositionMark());
		Paragraph pp = new Paragraph("\nFlight Details", fontP);
		pp.add(new Chunk(glue));
		pp.add("Customer Details:");
		document.add(pp);

		Font fontN = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontN.setSize(12);
		fontN.setColor(Color.BLACK);
		Chunk glueN = new Chunk(new VerticalPositionMark());
		Paragraph pN = new Paragraph(
				"Airplane: " + bookings.get(0).getFlight().getAirplane().getName(), fontN);
		pN.add(new Chunk(glueN));
		pN.add("Customer Name: " + bookings.get(0).getPassenger().getName());
		document.add(pN);

		Font fontA = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontA.setSize(12);
		fontA.setColor(Color.BLACK);
		Chunk glueA = new Chunk(new VerticalPositionMark());
		Paragraph pA = new Paragraph("Airplane Registration: " + bookings.get(0).getFlight().getAirplane().getRegistrationNumber(),
				fontA);
		pA.add(new Chunk(glueA));
		pA.add("Customer Mobile No: " + bookings.get(0).getPassenger().getContact());
		document.add(pA);

		Font fontBG = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontBG.setSize(12);
		fontBG.setColor(Color.BLACK);
		Paragraph pBG = new Paragraph(
				"Depature Airport: " + bookings.get(0).getFlight().getDepartureAirport().getName(), fontBG);
		pBG.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(pBG);

		Font fontE = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontE.setSize(12);
		fontE.setColor(Color.BLACK);
		Paragraph pE = new Paragraph(
				"Arrival Airport: " + bookings.get(0).getFlight().getArrivalAirport().getName(), fontE);
		pE.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(pE);
		
		Font fontDTime = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontDTime.setSize(12);
		fontDTime.setColor(Color.BLACK);
		Paragraph pDTime = new Paragraph(
				"Departure Time: " + DateTimeUtils.getProperDateTimeFormatFromEpochTime(bookings.get(0).getFlight().getDepartureTime()), fontDTime);
		pDTime.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(pDTime);

		Font fontATime = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontATime.setSize(12);
		fontATime.setColor(Color.BLACK);
		Paragraph pATime = new Paragraph(
				"Departure Time: " + DateTimeUtils.getProperDateTimeFormatFromEpochTime(bookings.get(0).getFlight().getArrivalTime()), fontATime);
		pATime.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(pATime);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(new Color(31, 53, 65));
		Paragraph p = new Paragraph("\nBooked Flight Seat Details\n", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 3.0f, 2.5f, 2.5f, 2.7f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);
		
		document.close();

	}

}
