package com.twoulaundry.assessment.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.twoulaundry.assessment.domain.enumeration.Currency;
import com.twoulaundry.assessment.domain.enumeration.InvoiceStatus;

/**
 * A DTO for the {@link com.twoulaundry.assessment.domain.Invoice} entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[0-9]+$")
    private String invoice_number;

    @NotNull
    private BigDecimal total;

    @NotNull
    private Currency currency;

    @NotNull
    private LocalDate invoice_date;

    private LocalDate due_date;

    private String vendor_name;

    private String remittance_address;

    private InvoiceStatus status = InvoiceStatus.PENDING;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(LocalDate invoice_date) {
        this.invoice_date = invoice_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getRemittance_address() {
        return remittance_address;
    }

    public void setRemittance_address(String remittance_address) {
        this.remittance_address = remittance_address;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (invoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", invoice_number='" + getInvoice_number() + "'" +
            ", total=" + getTotal() +
            ", currency='" + getCurrency() + "'" +
            ", invoice_date='" + getInvoice_date() + "'" +
            ", due_date='" + getDue_date() + "'" +
            ", vendor_name='" + getVendor_name() + "'" +
            ", remittance_address='" + getRemittance_address() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
