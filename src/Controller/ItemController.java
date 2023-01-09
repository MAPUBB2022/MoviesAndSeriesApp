package Controller;

import Model.*;
import Repository.IUserRepository;

import java.util.*;

public class ItemController {
    private IUserRepository userRepo;

    public ItemController(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (User u : userRepo.getAll())
            for (Order o : u.getOrders())
                for (Item i : o.getItems())
                    if (!items.contains(i))
                        items.add(i);
        return items;
    }

    public List<Movies> getMovies() {
        List<Movies> x = new ArrayList<>();
        for (Item i : getPredefinedItems()) {
            try {
                x.add((Movies) i);
            } catch (ClassCastException ignored) {
            }
        }
        return x;
    }

    public List<Series> getSeries()
    {
        List<Series> x = new ArrayList<>();
        for (Item i : getPredefinedItems())
        {
            try {
                x.add((Series) i);
            } catch (ClassCastException ignored)
            {
            }
        }
        return x;
    }

    public List<Movies> getMoviesSortedByRating()
    {
        List<Movies> res = getMovies();
        Collections.sort(res);
        return res;
    }

    public Series getLongestSeries()
    {
        Series s = getSeries().get(0);
        for (Series x : getSeries())
        {
            if (x.getNumberOfEpisodes() > s.getNumberOfEpisodes())
                s = x;
        }
        return s;
    }

    public Movies getHighestBudgetMovie()
    {
        Movies m = getMovies().get(0);
        for (Movies x : getMovies())
        {
            if (x.getBudget() > m.getBudget())
                m = x;
        }
        return m;
    }

    public List<Movies> getPredefinedMovies()
    {
        return userRepo.getPredefinedMovies();
    }

    public List<Series> getPredefinedSeries()
    {
        return userRepo.getPredefinedSeries();
    }

    public List<Item> getPredefinedItems()
    {
        List<Item> itemss = new ArrayList<>();
        itemss.addAll(getPredefinedMovies());
        itemss.addAll(getPredefinedSeries());
        return itemss;
    }

    public void addMovie(String title, double rating, int jahr, int duration, double budget)
    {
        userRepo.getPredefinedMovies().add(new Movies(title, rating, jahr, duration, budget));
    }
    public void addSeries(String title, double rating, int jahr, int numberOfEpisodes)
    {
        userRepo.getPredefinedSeries().add(new Series(title, rating, jahr, numberOfEpisodes));
    }
}
