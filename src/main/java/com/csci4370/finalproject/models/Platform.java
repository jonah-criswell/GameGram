package com.csci4370.finalproject.models;

public class Platform {
   private final String platform_name;
   private final String publisher;
   private final int year;
   private final double na_sales;
   private final double eu_sales;
   private final double jp_sales;
   private final double other_sales;
   private final double global_sales;

   public Platform(String platform_name, String publisher, int year, double na_sales, double eu_sales, double jp_sales, double other_sales, double global_sales) {
      this.platform_name = platform_name;
      this.publisher = publisher;
      this.year = year;
      this.na_sales = na_sales;
      this.eu_sales = eu_sales;
      this.jp_sales = jp_sales;
      this.other_sales = other_sales;
      this.global_sales = global_sales;
   }
   
   public String getPlatformName() {
      return platform_name;
   }
   
   public String getPublisher() {
      return publisher;
   }
   public int getYear() {
      return year;
   }
   public double getNaSales() {
      return na_sales;
   }
   public double getEuSales() {
      return eu_sales;
   }
   public double getJpSales() {
      return jp_sales;
   }
   public double getOtherSales() {
      return other_sales;
   }
}
