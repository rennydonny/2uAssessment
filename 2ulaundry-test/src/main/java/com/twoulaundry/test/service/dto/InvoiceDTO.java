package com.twoulaundry.test.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.twoulaundry.test.domain.enumeration.Currency;
import com.twoulaundry.test.domain.enumeration.InvoiceStatus;

/**
 * A DTO for the {@link com.twoulaundry.test.domain.Invoice} entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[0-9]+$")
    private String invoiceNumber;

    private BigDecimal total;

    private Currency currency = Currency.USD;

    private LocalDate invoiceDate = LocalDate.now();

    private LocalDate dueDate;

    private String vendorName;

    private String remittanceAddress;

    private InvoiceStatus status = InvoiceStatus.PENDING;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRemittanceAddress() {
        return remittanceAddress;
    }

    public void setRemittanceAddress(String remittanceAddress) {
        this.remittanceAddress = remittanceAddress;
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
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", total=" + getTotal() +
            ", currency='" + getCurrency() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", vendorName='" + getVendorName() + "'" +
            ", remittanceAddress='" + getRemittanceAddress() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
